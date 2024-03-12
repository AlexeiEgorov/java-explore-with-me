package ru.practicum.compilation.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.CompilationMapper;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CompilationRespDto;
import ru.practicum.event.controller.InitiatorsCategoriesLoader;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;

import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationServerController {
    private final CompilationService compilationService;
    private final InitiatorsCategoriesLoader initiatorsCategoriesLoader;
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationRespDto add(@RequestBody CompilationDto requestDto) {
        Compilation comp = compilationService.add(CompilationMapper.toCompilation(requestDto));
        comp.setEvents(eventService.findAllByIds(comp.getEvents().stream().map(Event::getId)
                .collect(Collectors.toSet())));
        CompilationRespDto resp = CompilationMapper.toDto(comp);
        resp.setEvents(initiatorsCategoriesLoader.loadPreviewResponseDtos(comp.getEvents()));
        return resp;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        compilationService.delete(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationRespDto patch(@PathVariable Long compId, @RequestBody CompilationDto updateDto) {
        Compilation comp = compilationService.patch(compId, updateDto);
        CompilationRespDto resp = CompilationMapper.toDto(comp);
        resp.setEvents(initiatorsCategoriesLoader.loadPreviewResponseDtos(comp.getEvents()));
        return resp;
    }

}
