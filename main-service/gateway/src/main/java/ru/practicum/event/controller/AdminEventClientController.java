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
import ru.practicum.dto.EventResponseDto;
import ru.practicum.event.EventClient;
import ru.practicum.event.model.EventPatchDto;
import ru.practicum.event.model.StartAfterTwoHoursFromNow;
import ru.practicum.model.ConstraintViolationException;
import ru.practicum.model.EventStatus;
import ru.practicum.model.NotAllowedActionException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.FORMATTER;

@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/events")
public class AdminEventClientController {
    private final EventClient client;
    private final ObjectMapper objectMapper;
    private final ViewsLoader viewsLoader;

    @GetMapping
    public ResponseEntity<Object> searchEvents(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<EventStatus> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        if (rangeStart != null && rangeEnd != null) {
            LocalDateTime start = LocalDateTime.parse(rangeStart, FORMATTER);
            LocalDateTime end = LocalDateTime.parse(rangeEnd, FORMATTER);
            if (end.isBefore(start)) {
                throw new NotAllowedActionException("End time cannot go before start time");
            }
        }
        ResponseEntity<Object> resp = client.searchEventsForAdmin(users, states, categories, rangeStart, rangeEnd,
                from, size);
        if (resp.getStatusCode() == HttpStatus.OK) {
            List<EventResponseDto> eventDtos = objectMapper.convertValue(resp.getBody(), new TypeReference<>() {});
            return ResponseEntity.ok(viewsLoader.loadViewsForEventDtos(eventDtos));
        }
        return resp;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> reviewEvent(@PathVariable Long id,
                                              @RequestBody @Valid EventPatchDto eventPatchDto) {
        if (id < 1) {
            throw new ConstraintViolationException("Id should be positive");
        }
        if (eventPatchDto.getEventDate() != null) {
            @StartAfterTwoHoursFromNow
            LocalDateTime time = LocalDateTime.parse(eventPatchDto.getEventDate(), FORMATTER);
        }
        return client.reviewEvent(id, eventPatchDto);
    }

}
