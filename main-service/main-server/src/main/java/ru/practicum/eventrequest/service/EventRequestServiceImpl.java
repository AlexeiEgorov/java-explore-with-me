package ru.practicum.eventrequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventRequestStatus;
import ru.practicum.event.model.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.eventrequest.EventRequestRepository;
import ru.practicum.eventrequest.model.EventRequest;
import ru.practicum.model.ConstraintViolationException;
import ru.practicum.model.EventStatus;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.model.NotAllowedActionException;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.LocalConstants.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventRequestServiceImpl implements EventRequestService {
    private final EventRequestRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public EventRequest add(Long userId, Long eventId) {
        Event event = getEvent(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConstraintViolationException("Initiator can't make a request for his own event.");
        }
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new ConstraintViolationException("The event's not yet accepted by the administrators.");
        }
        if (event.getConfirmedRequests().equals(event.getParticipantLimit()) && event.getParticipantLimit() != 0) {
            throw new ConstraintViolationException("The participant limit is reached.");
        }
        EventRequest previousRequest = repository.findByEventIdAndRequesterId(eventId, userId);
        if (previousRequest != null) {
            throw new ConstraintViolationException("You've already made a request for this event.");
        }
        EventRequestStatus state;
        if (event.getRequestModeration().equals(false) || event.getParticipantLimit().equals(0)) {
            state = EventRequestStatus.CONFIRMED;
            eventRepository.incrementConfirmedRequests(eventId);
        } else {
            state = EventRequestStatus.PENDING;
        }
        EventRequest eventRequest = new EventRequest(null, LocalDateTime.now(), event,
                new User(userId, null, null), state);
        return repository.save(eventRequest);
    }

    @Override
    public List<EventRequest> getUserRequests(Long userId) {
        return repository.findAllByRequesterId(userId);
    }

    @Override
    @Transactional
    public EventRequest cancel(Long userId, Long requestId) {
        EventRequest eventRequest = get(requestId);
        if (!eventRequest.getStatus().equals(EventRequestStatus.PENDING)) {
            throw new NotAllowedActionException("Request without pending status, cannot be changed");
        }
        eventRequest.setStatus(EventRequestStatus.CANCELED);
        repository.cancel(requestId, EventRequestStatus.CANCELED);
        return eventRequest;
    }

    @Override
    public EventRequest get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(REQUEST, id));
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(USER, id));
    }

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EVENT, id));
    }
}
