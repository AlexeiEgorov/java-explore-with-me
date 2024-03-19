package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.CommentsCountLoader;
import ru.practicum.ConfirmedRequestsLoader;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.dto.EventPatchDto;
import ru.practicum.dto.EventResponseDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.service.EventService;
import ru.practicum.model.EventStatus;
import ru.practicum.user.model.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.DATE_TIME_FORMAT;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventServerController {
    private final EventService service;
    private final InitiatorsCategoriesLoader initiatorsCategoriesLoader;
    private final ConfirmedRequestsLoader confirmedRequestsLoader;
    private final CommentsCountLoader commentsCountLoader;

    @GetMapping
    public List<EventResponseDto>  searchEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventStatus> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<Event> events = service.searchEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventResponseDto> resp = initiatorsCategoriesLoader.loadFullResponseDtos(events);
        commentsCountLoader.loadForEventResponseDtos(resp);
        return confirmedRequestsLoader.loadForEventDtos(resp);
    }

    @PatchMapping("/{id}")
    public EventResponseDto reviewEvent(@PathVariable Long id, @RequestBody EventPatchDto eventPatchDto) {
        Event event = service.reviewEvent(id, eventPatchDto);
        EventResponseDto resp = EventMapper.toResponseDto(event);
        resp.setCategory(CategoryMapper.toEventCategoryDto(event.getCategory()));
        resp.setInitiator(UserMapper.toInitiator(event.getInitiator()));
        List<EventResponseDto> respList = List.of(resp);
        commentsCountLoader.loadForEventResponseDtos(respList);
        return confirmedRequestsLoader.loadForEventDtos(respList).get(0);
    }

}
