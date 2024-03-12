package ru.practicum.eventrequest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventrequest.dto.ResponseEventRequestDto;
import ru.practicum.eventrequest.model.EventRequestMapper;
import ru.practicum.eventrequest.service.EventRequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class UserEventRequestServerController {
    private final EventRequestService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEventRequestDto add(@PathVariable Long userId, @RequestParam Long eventId) {
        return EventRequestMapper.toResponseDto(service.add(userId, eventId));
    }

    @GetMapping
    public List<ResponseEventRequestDto> getUserRequests(@PathVariable Long userId) {
        return service.getUserRequests(userId).stream().map(EventRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEventRequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return EventRequestMapper.toResponseDto(service.cancel(userId, requestId));
    }
}
