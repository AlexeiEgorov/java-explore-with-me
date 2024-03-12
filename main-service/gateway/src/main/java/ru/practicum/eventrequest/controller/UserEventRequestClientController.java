package ru.practicum.eventrequest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventrequest.EventRequestClient;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class UserEventRequestClientController {
    private final EventRequestClient client;

    @PostMapping
    public ResponseEntity<Object> add(@PathVariable Long userId, @RequestParam Long eventId) {
        return client.add(userId, eventId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@PathVariable Long userId) {
        return client.getUserRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<Object> cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return client.cancel(userId, requestId);
    }
}
