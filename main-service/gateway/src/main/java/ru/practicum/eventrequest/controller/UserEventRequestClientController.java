package ru.practicum.eventrequest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventrequest.EventRequestClient;
import ru.practicum.model.ConstraintViolationException;

import static ru.practicum.Constants.POSITIVE_ID_CONS;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class UserEventRequestClientController {
    private final EventRequestClient client;

    @PostMapping
    public ResponseEntity<Object> add(@PathVariable Long userId, @RequestParam Long eventId) {
        if (eventId < 1) {
            throw new ConstraintViolationException(POSITIVE_ID_CONS);
        }
        return client.add(userId, eventId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@PathVariable Long userId) {
        return client.getUserRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<Object> cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        if (requestId < 1) {
            throw new ConstraintViolationException(POSITIVE_ID_CONS);
        }
        return client.cancel(userId, requestId);
    }
}
