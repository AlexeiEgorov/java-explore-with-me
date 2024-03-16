package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ConfirmedRequestsLoader;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.dto.EventPreviewResponseDto;
import ru.practicum.dto.EventResponseDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.service.EventService;
import ru.practicum.model.SortType;
import ru.practicum.user.model.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.DATE_TIME_FORMAT;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventServerController {
    private final EventService service;
    private final InitiatorsCategoriesLoader initiatorsCategoriesLoader;
    private final ConfirmedRequestsLoader confirmedRequestsLoader;

    @GetMapping
    public List<EventPreviewResponseDto> searchEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") SortType sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<Event> events = service.searchEventsForVisitor(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
        List<EventPreviewResponseDto> resp = initiatorsCategoriesLoader.loadPreviewResponseDtos(events);
        return confirmedRequestsLoader.loadForEventDtos(resp);

    }

    @GetMapping("/{id}")
    public EventResponseDto getEventForVisitor(@PathVariable Long id) {
        Event event = service.getEventForVisitor(id);
        EventResponseDto resp = EventMapper.toResponseDto(event);
        resp.setCategory(CategoryMapper.toEventCategoryDto(event.getCategory()));
        resp.setInitiator(UserMapper.toInitiator(event.getInitiator()));
        confirmedRequestsLoader.loadForEventDtos(List.of(resp));
        return resp;
    }
}
