package ru.practicum.compilation.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.CommentsCountLoader;
import ru.practicum.ConfirmedRequestsLoader;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.CompilationMapper;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.dto.CompilationRespDto;
import ru.practicum.dto.EventPreviewResponseDto;
import ru.practicum.event.controller.InitiatorsCategoriesLoader;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicCompilationServerController {
    private final CompilationService service;
    private final EventService eventService;
    private final InitiatorsCategoriesLoader initiatorsCategoriesLoader;
    private final ConfirmedRequestsLoader confirmedRequestsLoader;
    private final CommentsCountLoader commentsCountLoader;

    @GetMapping
    public List<CompilationRespDto> getCompilations(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Boolean pinned
    ) {
        List<Compilation> compilations = service.getCompilations(from, size, pinned);
        Map<Long, EventPreviewResponseDto> eventDtos = new HashMap<>();
        for (Compilation comp : compilations) {
            for (Event event : comp.getEvents()) {
                eventDtos.put(event.getId(), null);
            }
        }
        Set<EventPreviewResponseDto> events = initiatorsCategoriesLoader.loadPreviewResponseDtos(
                eventService.findAllByIds(eventDtos.keySet()));
        for (EventPreviewResponseDto dto : confirmedRequestsLoader.loadForEventDtos(events)) {
            eventDtos.put(dto.getId(), dto);
        }
        commentsCountLoader.loadForEventPreviewDtos(eventDtos.values());
        List<CompilationRespDto> resp = new ArrayList<>();
        for (Compilation comp : compilations) {
            CompilationRespDto compDto = CompilationMapper.toDto(comp);
            for (Event event : comp.getEvents()) {
                compDto.getEvents().add(eventDtos.get(event.getId()));
            }
            resp.add(compDto);
        }
        return resp;
    }

    @GetMapping("/{compId}")
    public CompilationRespDto getCompilation(@PathVariable Long compId) {
        Compilation comp = service.getCompilation(compId);
        CompilationRespDto resp = CompilationMapper.toDto(comp);
        resp.setEvents(initiatorsCategoriesLoader.loadPreviewResponseDtos(comp.getEvents()));
        commentsCountLoader.loadForEventPreviewDtos(resp.getEvents());
        confirmedRequestsLoader.loadForEventDtos(resp.getEvents());
        return resp;
    }

}
