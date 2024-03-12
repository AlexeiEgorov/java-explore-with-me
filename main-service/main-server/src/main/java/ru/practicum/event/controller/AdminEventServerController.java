package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.dto.EventResponseDto;
import ru.practicum.event.dto.EventPatchDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.service.EventService;
import ru.practicum.model.EventStatus;
import ru.practicum.user.model.UserMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventServerController {
    private final EventService service;
    private final InitiatorsCategoriesLoader initiatorsCategoriesLoader;

    @GetMapping
    public List<EventResponseDto>  searchEvents(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<EventStatus> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<Event> events = service.searchEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
        return initiatorsCategoriesLoader.loadFullResponseDtos(events);
    }

    @PatchMapping("/{id}")
    public EventResponseDto reviewEvent(@PathVariable Long id, @RequestBody EventPatchDto eventPatchDto) {
        Event event = service.reviewEvent(id, eventPatchDto);
        EventResponseDto resp = EventMapper.toResponseDto(event);
        resp.setCategory(CategoryMapper.toEventCategoryDto(event.getCategory()));
        resp.setInitiator(UserMapper.toInitiator(event.getInitiator()));
        return resp;
    }

}
