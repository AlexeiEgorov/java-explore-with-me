package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.dto.EventPreviewResponseDto;
import ru.practicum.dto.EventResponseDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.service.EventService;
import ru.practicum.model.SortType;
import ru.practicum.user.model.UserMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventServerController {
    private final EventService service;
    private final InitiatorsCategoriesLoader initiatorsCategoriesLoader;

    @GetMapping
    public List<EventPreviewResponseDto> searchEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") SortType sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<Event> events = service.searchEventsForVisitor(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);

        return initiatorsCategoriesLoader.loadPreviewResponseDtos(events);
    }

    @GetMapping("/{id}")
    public EventResponseDto getEventForVisitor(@PathVariable Long id) {
        Event event = service.getEventForVisitor(id);
        EventResponseDto resp = EventMapper.toResponseDto(event);
        resp.setCategory(CategoryMapper.toEventCategoryDto(event.getCategory()));
        resp.setInitiator(UserMapper.toInitiator(event.getInitiator()));
        return resp;
    }
}
