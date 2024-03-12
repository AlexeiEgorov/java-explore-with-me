package ru.practicum.eventrequest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventrequest.EventRequestClient;

import javax.validation.constraints.Positive;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class UserEventRequestClientController {
    private final EventRequestClient client;

    @PostMapping
    public ResponseEntity<Object> add(@PathVariable Long userId, @RequestParam @Positive Long eventId) {
        return client.add(userId, eventId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@PathVariable Long userId) {
        return client.getUserRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<Object> cancel(@PathVariable Long userId, @PathVariable @Positive Long requestId) {
        return client.cancel(userId, requestId);
    }
}
