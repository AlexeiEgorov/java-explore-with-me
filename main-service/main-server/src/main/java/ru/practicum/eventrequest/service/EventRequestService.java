package ru.practicum.eventrequest.service;

import ru.practicum.event.model.Event;
import ru.practicum.eventrequest.model.ConfirmedRequests;
import ru.practicum.eventrequest.model.EventRequest;

import java.util.List;
import java.util.Set;

public interface EventRequestService {

    EventRequest get(Long id);

    Event getEvent(Long id);

    EventRequest add(Long userId, Long eventId);

    List<EventRequest> getUserRequests(Long userId);

    EventRequest cancel(Long userId, Long requestId);

    List<ConfirmedRequests> getConfirmedRequestsForEvents(Set<Long> eventsIds);
}
