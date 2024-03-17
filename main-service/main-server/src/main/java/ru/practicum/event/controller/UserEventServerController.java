package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.CommentsLoader;
import ru.practicum.ConfirmedRequestsLoader;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.dto.EventDto;
import ru.practicum.dto.EventPatchDto;
import ru.practicum.dto.EventRequestsConfirmationDto;
import ru.practicum.dto.EventResponseDto;
import ru.practicum.event.dto.EventRequestsConfirmationResultDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.service.EventService;
import ru.practicum.eventrequest.dto.ResponseEventRequestDto;
import ru.practicum.eventrequest.model.EventRequestMapper;
import ru.practicum.user.model.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class UserEventServerController {
    private final EventService service;
    private final InitiatorsCategoriesLoader initiatorsCategoriesLoader;
    private final ConfirmedRequestsLoader confirmedRequestsLoader;
    private final CommentsLoader commentsLoader;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto add(@PathVariable Long userId, @RequestBody EventDto eventDto) {
        Event event = service.save(userId, eventDto);
        EventResponseDto resp = EventMapper.toResponseDto(event);
        resp.setCategory(CategoryMapper.toEventCategoryDto(event.getCategory()));
        resp.setInitiator(UserMapper.toInitiator(event.getInitiator()));
        return resp;
    }

    @GetMapping
    public List<EventResponseDto> getUserEvents(@PathVariable Long userId,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        List<Event> events = service.getUserEvents(userId, from, size);
        List<EventResponseDto> resp = initiatorsCategoriesLoader.loadFullResponseDtos(events);
        commentsLoader.loadForEventResponseDtos(resp);
        return confirmedRequestsLoader.loadForEventDtos(resp);
    }

    @GetMapping("/{id}")
    public EventResponseDto getUserEvent(@PathVariable Long userId, @PathVariable Long id) {
        Event event = service.getUserEvent(userId, id);
        EventResponseDto resp = EventMapper.toResponseDto(event);
        resp.setCategory(CategoryMapper.toEventCategoryDto(event.getCategory()));
        resp.setInitiator(UserMapper.toInitiator(event.getInitiator()));
        List<EventResponseDto> respList = List.of(resp);
        commentsLoader.loadForEventResponseDtos(respList);
        return confirmedRequestsLoader.loadForEventDtos(respList).get(0);
    }

    @PatchMapping("/{id}")
    public EventResponseDto patch(@PathVariable Long userId, @PathVariable Long id,
                                  @RequestBody EventPatchDto eventPatchDto) {
        Event event = service.patch(userId, id, eventPatchDto);
        EventResponseDto resp = EventMapper.toResponseDto(event);
        resp.setCategory(CategoryMapper.toEventCategoryDto(event.getCategory()));
        resp.setInitiator(UserMapper.toInitiator(event.getInitiator()));
        List<EventResponseDto> respList = List.of(resp);
        commentsLoader.loadForEventResponseDtos(respList);
        return confirmedRequestsLoader.loadForEventDtos(respList).get(0);
    }

    @GetMapping("/{id}/requests")
    public List<ResponseEventRequestDto> getEventRequests(@PathVariable Long userId, @PathVariable Long id) {
        return service.getEventRequests(userId, id).stream()
                .map(EventRequestMapper::toResponseDto).collect(Collectors.toList());
    }

    @PatchMapping("/{id}/requests")
    public EventRequestsConfirmationResultDto updateEventRequestsStatuses(@PathVariable Long userId,
                                                                          @PathVariable Long id,
                                                                          @RequestBody
                                                                              EventRequestsConfirmationDto
                                                                                      eventRequestsConfirmationDto) {
        return service.updateEventRequestsStatuses(userId, id, eventRequestsConfirmationDto);
    }


}
