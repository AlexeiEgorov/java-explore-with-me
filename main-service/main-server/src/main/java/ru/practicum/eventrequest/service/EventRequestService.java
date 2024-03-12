package ru.practicum.eventrequest.service;

import ru.practicum.event.model.Event;
import ru.practicum.eventrequest.model.EventRequest;
import ru.practicum.user.model.User;

import java.util.List;

public interface EventRequestService {

    EventRequest get(Long id);

    User getUser(Long id);

    Event getEvent(Long id);

    EventRequest add(Long userId, Long eventId);

    List<EventRequest> getUserRequests(Long userId);

    EventRequest cancel(Long userId, Long requestId);
}
