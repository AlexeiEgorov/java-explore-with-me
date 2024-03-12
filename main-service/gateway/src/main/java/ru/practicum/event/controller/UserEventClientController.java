package ru.practicum.event.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ViewsLoader;
import ru.practicum.dto.EventRequestsConfirmationDto;
import ru.practicum.dto.EventResponseDto;
import ru.practicum.event.EventClient;
import ru.practicum.event.model.EventDto;
import ru.practicum.event.model.EventPatchDto;
import ru.practicum.event.model.StartAfterTwoHoursFromNow;
import ru.practicum.model.ConstraintViolationException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.FORMATTER;

@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{userId}/events")
public class UserEventClientController {
    private final EventClient client;
    private final ViewsLoader viewsLoader;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid EventDto eventDto, @PathVariable Long userId) {
        if (eventDto.getEventDate() != null) {
            LocalDateTime time = LocalDateTime.parse(eventDto.getEventDate(), FORMATTER);
            if (!time.isAfter(LocalDateTime.now().plusHours(2))) {
                throw new ConstraintViolationException("Start of event cannot be earlier than two hours in the future");
            }
        }

        return client.add(userId, eventDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserEvents(@PathVariable Long userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        ResponseEntity<Object> resp = client.getUserEvents(userId, from, size);
        if (resp.getStatusCode() == HttpStatus.OK) {
            List<EventResponseDto> eventDtos = objectMapper.convertValue(resp.getBody(), new TypeReference<>() {});
            return ResponseEntity.ok(viewsLoader.loadViewsForEventDtos(eventDtos));
        }
        return resp;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserEvent(@PathVariable Long userId, @PathVariable Long id) {
        ResponseEntity<Object> resp = client.getUserEvent(userId, id);
        if (resp.getStatusCode() == HttpStatus.OK) {
            EventResponseDto eventDto = objectMapper.convertValue(resp.getBody(), EventResponseDto.class);
            return ResponseEntity.ok(viewsLoader.loadViewsForEventDtos(List.of(eventDto)).get(0));
        }
        return resp;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(@PathVariable Long userId, @PathVariable Long id,
                                        @Valid @RequestBody EventPatchDto eventPatchDto) {
        if (eventPatchDto.getEventDate() != null) {
            @StartAfterTwoHoursFromNow
            LocalDateTime time = LocalDateTime.parse(eventPatchDto.getEventDate(), FORMATTER);
        }
        return client.patch(userId, id, eventPatchDto);
    }

    @GetMapping("/{id}/requests")
    public ResponseEntity<Object> getEventRequests(@PathVariable Long userId, @PathVariable Long id) {
        return client.getEventRequests(userId, id);
    }

    @PatchMapping("/{id}/requests")
    public ResponseEntity<Object> updateEventRequestsStatuses(@PathVariable Long userId,
                                                              @PathVariable Long id,
                                                              @RequestBody @Valid
                                                              EventRequestsConfirmationDto
                                                                      eventRequestsConfirmationDto) {
        return client.updateEventRequestsStatuses(userId, id, eventRequestsConfirmationDto);
    }
}
