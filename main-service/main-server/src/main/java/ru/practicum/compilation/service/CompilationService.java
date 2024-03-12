package ru.practicum.compilation.service;

import ru.practicum.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {
    Compilation add(Compilation requestDto);

    void delete(Long compId);

    Compilation patch(Long compId, CompilationDto updateDto);

    Compilation get(Long compId);

    List<Compilation> getCompilations(Integer from, Integer size, Boolean pinned);

    Compilation getCompilation(Long compId);
}
