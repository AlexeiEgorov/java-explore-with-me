package ru.practicum.event.service;

import ru.practicum.category.model.Category;
import ru.practicum.dto.EventRequestsConfirmationDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventPatchDto;
import ru.practicum.event.dto.EventRequestsConfirmationResultDto;
import ru.practicum.event.model.Event;
import ru.practicum.eventrequest.model.EventRequest;
import ru.practicum.model.EventStatus;
import ru.practicum.model.SortType;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Set;

public interface EventService {

    Event patch(Long userId, Long id, EventPatchDto eventPatchDto);

    Category getCategory(Long id);

    User getUser(Long id);

    List<Event> searchEventsForAdmin(
            List<Integer> users,
            List<EventStatus> states,
            List<Integer> categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size
    );

    Event save(Long userId, EventDto event);

    Event getUserEvent(Long userId, Long id);

    Event get(Long id);

    List<Event> getUserEvents(Long userId, Integer from, Integer size);

    List<EventRequest> getEventRequests(Long userId, Long id);

    EventRequestsConfirmationResultDto updateEventRequestsStatuses(Long userId, Long id,
                                                                   EventRequestsConfirmationDto
                                                                           eventRequestsConfirmationDto);

    Event reviewEvent(Long id, EventPatchDto eventPatchDto);

    List<Event> searchEventsForVisitor(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                       String rangeEnd, Boolean onlyAvailable, SortType sort, Integer from,
                                       Integer size);

    Event getEventForVisitor(Long id);

    List<Event> findAllByIds(Set<Long> sets);
}
