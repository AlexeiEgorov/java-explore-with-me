package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ConfirmedRequestsLoader;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.dto.*;
import ru.practicum.event.EventRepository;
import ru.practicum.event.dto.EventRequestsConfirmationResultDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.model.EventSpecifications;
import ru.practicum.eventrequest.EventRequestRepository;
import ru.practicum.eventrequest.dto.ResponseEventRequestDto;
import ru.practicum.eventrequest.model.EventRequest;
import ru.practicum.eventrequest.model.EventRequestMapper;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.model.*;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.LocalConstants.CATEGORY;
import static ru.practicum.LocalConstants.EVENT;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final CategoryRepository categoryRepository;
    private final EventRequestRepository eventRequestRepository;
    private final ConfirmedRequestsLoader confirmedRequestsLoader;
    private static final String NO_EVENTS_CHANGING_PERMISSION = "You have not admin permission to change published/" +
            "rejected event.";
    private static final String PUBLISHED_E_STAT_CANNOT_BE_CHANGED = "Status of a published event cannot be changed.";
    private static final String LESS_THAN_AN_HOUR_LEFT_EXP = "Event cannot be published if less than an hour left " +
            "before it starts!";

    @Override
    public List<Event> searchEventsForAdmin(
            List<Long> users,
            List<EventStatus> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    ) {
        Specification<Event> specification = Specification.where(null);

        if (users != null && !users.isEmpty()) {
            specification = specification.and(EventSpecifications.hasUsers(users));
        }
        if (states != null && !states.isEmpty()) {
            specification = specification.and(EventSpecifications.hasStates(states));
        }
        if (categories != null && !categories.isEmpty()) {
            specification = specification.and(EventSpecifications.hasCategories(categories));
        }
        if (rangeStart != null) {
            specification = specification.and(EventSpecifications.hasEventDateAfter(rangeStart));
        }
        if (rangeEnd != null) {
            specification = specification.and(EventSpecifications.hasEventDateBefore(rangeEnd));
        }
        PageRequest pageRequest = PageRequest.of(from, size);

        return repository.findAll(specification, pageRequest).getContent();
    }

    @Override
    @Transactional
    public Event save(Long userId, EventDto eventDto) {
        Category category = getCategory(eventDto.getCategory());
        Event event = EventMapper.toEvent(eventDto);
        event.setCategory(category);
        event.setInitiator(new User(userId, null, null));
        event.setCreatedOn(LocalDateTime.now());
        return repository.save(event);
    }

    @Override
    public List<Event> getUserEvents(Long userId, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return repository.findAllByInitiatorId(userId, pageRequest).getContent();
    }

    @Override
    public List<EventRequest> getEventRequests(Long userId, Long id) {
        get(id);
        return eventRequestRepository.findAllByEventId(id);
    }

    private List<Long> updateRequestsStatusesAndGetIds(List<EventRequest> eventRequests,
                                                       EventRequestStatus eventStatus) {
        List<Long> ids = new ArrayList<>();
        for (EventRequest request : eventRequests) {
            if (!request.getStatus().equals(EventRequestStatus.PENDING)) {
                throw new NotAllowedActionException(
                        String.format("Request with id - %d, can't be updated, it's already %s.",
                                request.getId(), request.getStatus().name()));
            }
            request.setStatus(eventStatus);
            ids.add(request.getId());
        }
        return ids;
    }

    @Override
    @Transactional
    public EventRequestsConfirmationResultDto updateEventRequestsStatuses(Long userId, Long id,
                                                                          EventRequestsConfirmationDto
                                                                                  confirmationDto) {
        Event event = get(id);
        Long confirmedReqs = confirmedRequestsLoader.getConfirmedCountForEvent(event);
        if (confirmedReqs.equals(event.getParticipantLimit())
                && !event.getParticipantLimit().equals(0L)) {
            throw new ConstraintViolationException("The participant limit has been reached.");
        }
        EventRequestStatus eventStatusToAssign = EventRequestStatus.valueOf(confirmationDto.getStatus().name());
        List<EventRequest> selectedRequests =
                eventRequestRepository.findAllById(confirmationDto.getRequestIds());
        long confirmedRequestsAfterAdding = 0;
        if (eventStatusToAssign.equals(EventRequestStatus.CONFIRMED)) {
            confirmedRequestsAfterAdding = confirmedReqs + selectedRequests.size();
        }

        if (event.getParticipantLimit().equals(0L)
                || eventStatusToAssign.equals(EventRequestStatus.REJECTED)
                || (confirmedRequestsAfterAdding <= event.getParticipantLimit())) {
            List<Long> requestsIdsToUpdate = updateRequestsStatusesAndGetIds(selectedRequests, eventStatusToAssign);

            EventRequestsConfirmationResultDto conformationResultDto = new EventRequestsConfirmationResultDto();
            List<ResponseEventRequestDto> selectedRequestsDtos = selectedRequests.stream()
                    .map(EventRequestMapper::toResponseDto).collect(Collectors.toList());

            if (eventStatusToAssign.equals(EventRequestStatus.CONFIRMED)) {
                conformationResultDto.setConfirmedRequests(selectedRequestsDtos);
                conformationResultDto.setRejectedRequests(List.of());
            } else {
                conformationResultDto.setRejectedRequests(selectedRequestsDtos);
                conformationResultDto.setConfirmedRequests(List.of());
            }
            eventRequestRepository.updateStatusOfEventRequestsByIds(requestsIdsToUpdate, eventStatusToAssign);

            return conformationResultDto;

        } else {
            int requestsLeftToAdd = (int) (event.getParticipantLimit() - confirmedReqs);
            List<EventRequest> confirmedRequests = selectedRequests.subList(0, requestsLeftToAdd);
            List<EventRequest> rejectedRequests = selectedRequests.subList(requestsLeftToAdd, selectedRequests.size());
            List<Long> confirmedReqIds = updateRequestsStatusesAndGetIds(confirmedRequests,
                    EventRequestStatus.CONFIRMED);
            List<Long> rejectedReqIds = updateRequestsStatusesAndGetIds(rejectedRequests,
                    EventRequestStatus.REJECTED);

            EventRequestsConfirmationResultDto conformationResultDto = new EventRequestsConfirmationResultDto();
            conformationResultDto.setConfirmedRequests(confirmedRequests.stream().map(EventRequestMapper::toResponseDto)
                    .collect(Collectors.toList()));
            conformationResultDto.setRejectedRequests(rejectedRequests.stream().map(EventRequestMapper::toResponseDto)
                    .collect(Collectors.toList()));
            eventRequestRepository.updateStatusOfEventRequestsByIds(confirmedReqIds, EventRequestStatus.CONFIRMED);
            eventRequestRepository.updateStatusOfEventRequestsByIds(rejectedReqIds, EventRequestStatus.REJECTED);

            return conformationResultDto;
        }
    }

    @Override
    @Transactional
    public Event reviewEvent(Long id, ru.practicum.dto.EventPatchDto eventPatchDto) {
        Event event = get(id);
        if (eventPatchDto.getStateAction() != null) {
            if (event.getState().equals(EventStatus.PENDING)) {
                if (eventPatchDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                    if (!LocalDateTime.now().plusHours(1).isBefore(event.getEventDate())) {
                        throw new ConstraintViolationException(LESS_THAN_AN_HOUR_LEFT_EXP);
                    }
                    event.setState(EventStatus.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else if (eventPatchDto.getStateAction().equals(StateAction.REJECT_EVENT)) {
                    event.setState(EventStatus.REJECTED);
                } else {
                    event.setState(EventStatus.PENDING);
                }
            } else {
                throw new ConstraintViolationException(PUBLISHED_E_STAT_CANNOT_BE_CHANGED);
            }
        }
        patchEvent(event, eventPatchDto);

        return repository.save(event);
    }

    @Override
    public List<Event> searchEventsForVisitor(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd, Boolean onlyAvailable, SortType sort, Integer from,
                                              Integer size) {
        Specification<Event> specification = Specification.where(null);

        if (text != null) {
            specification = specification.and(EventSpecifications.containsText(text));
        }
        if (categories != null && !categories.isEmpty()) {
            specification = specification.and(EventSpecifications.hasCategories(categories));
        }
        if (paid != null) {
            specification = specification.and(EventSpecifications.isPaid(paid));
        }
        if (rangeStart != null) {
            specification = specification.and(EventSpecifications.hasEventDateAfter(rangeStart));
        }
        if (rangeEnd != null) {
            specification = specification.and(EventSpecifications.hasEventDateBefore(rangeEnd));
        }
        if (onlyAvailable != null && onlyAvailable) {
            specification = specification.and(EventSpecifications.isAvailable());
        }
        Sort eventSort = Sort.by(sort.equals(SortType.VIEWS) ? "views" : "eventDate");
        Pageable pageable = PageRequest.of(from, size, eventSort);

        return repository.findAll(specification, pageable).getContent();
    }

    @Override
    public Event getEventForVisitor(Long id) {
        Event event = get(id);
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new EntityNotFoundException(EVENT, id);
        }
        return event;
    }

    private void patchEvent(Event event, EventPatchDto eventPatchDto) {
        if (eventPatchDto.getAnnotation() != null && !eventPatchDto.getAnnotation().isBlank()) {
            event.setAnnotation(eventPatchDto.getAnnotation());
        }
        if (eventPatchDto.getCategory() != null) {
            event.setCategory(getCategory(eventPatchDto.getCategory()));
        }
        if (eventPatchDto.getDescription() != null && !eventPatchDto.getDescription().isBlank()) {
            event.setDescription(eventPatchDto.getDescription());
        }
        if (eventPatchDto.getEventDate() != null) {
            LocalDateTime time = eventPatchDto.getEventDate();
            if (!time.isAfter(LocalDateTime.now())) {
                throw new NotAllowedActionException("Event cannot start in the past");
            }
            event.setEventDate(time);
        }
        if (eventPatchDto.getLocation() != null) {
            event.setLocation(EventMapper.toLocation(eventPatchDto.getLocation()));
        }
        if (eventPatchDto.getPaid() != null) {
            event.setPaid(eventPatchDto.getPaid());
        }
        if (eventPatchDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventPatchDto.getParticipantLimit());
        }
        if (eventPatchDto.getRequestModeration() != null) {
            event.setRequestModeration(eventPatchDto.getRequestModeration());
        }
        if (eventPatchDto.getTitle() != null && !eventPatchDto.getTitle().isBlank()) {
            event.setTitle(eventPatchDto.getTitle());
        }
    }

    @Override
    @Transactional
    public Event patch(Long userId, Long id, ru.practicum.dto.EventPatchDto eventPatchDto) {
        Event event = get(id);
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            throw new ConstraintViolationException(PUBLISHED_E_STAT_CANNOT_BE_CHANGED);
        }
        if (eventPatchDto.getStateAction() != null) {
            if (eventPatchDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                throw new ConstraintViolationException(NO_EVENTS_CHANGING_PERMISSION);
            }
            if (eventPatchDto.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(EventStatus.CANCELED);
            } else {
                event.setState(EventStatus.PENDING);
            }
        }
        patchEvent(event, eventPatchDto);

        return repository.save(event);
    }

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(CATEGORY, id));
    }

    @Override
    public Event get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(EVENT, id));
    }

    @Override
    public Event getUserEvent(Long userId, Long id) {
        return get(id);
    }

    @Override
    public Set<Event> findAllByIds(Set<Long> ids) {
        return repository.findAllByIdIn(ids);
    }

    @Override
    public List<EventForComment> findAllEventForComment(Set<Long> ids) {
        return repository.getEventForCommentByIds(ids);
    }
}
