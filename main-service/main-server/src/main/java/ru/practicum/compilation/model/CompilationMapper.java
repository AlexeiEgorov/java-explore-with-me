package ru.practicum.compilation.model;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CompilationRespDto;
import ru.practicum.event.model.Event;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(CompilationDto dto) {
        Boolean pinned = dto.getPinned() != null ? dto.getPinned() : false;
        Compilation compilation = new Compilation(null, dto.getTitle(), pinned, null);
        if (dto.getEvents() != null) {
            compilation.setEvents(dto.getEvents().stream()
                    .map(Event::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        } else {
            compilation.setEvents(Set.of());
        }
        return compilation;
    }

    public static CompilationRespDto toDto(Compilation compilation) {
        return new CompilationRespDto(compilation.getId(), compilation.getTitle(), compilation.getPinned(),
                new LinkedHashSet<>());
    }
}
