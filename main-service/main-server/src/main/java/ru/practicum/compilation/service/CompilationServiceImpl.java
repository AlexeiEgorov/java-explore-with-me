package ru.practicum.compilation.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.CompilationRepository;
import ru.practicum.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.model.Event;
import ru.practicum.exception.EntityNotFoundException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.LocalConstants.COMPILATION;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;


    @Override
    @Transactional
    public Compilation add(Compilation compilation) {
        return repository.save(compilation);
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        repository.delete(get(compId));
    }

    @Override
    @Transactional
    public Compilation patch(Long compId, CompilationDto dto) {
        Compilation compilation = get(compId);
        if (dto.getEvents() != null) {
            compilation.setEvents(dto.getEvents().stream()
                    .map(Event::new)
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }
        if (dto.getTitle() != null) {
            compilation.setTitle(dto.getTitle());
        }

        return repository.save(compilation);
    }

    @Override
    public Compilation get(Long compId) {
        return repository.findById(compId).orElseThrow(() -> new EntityNotFoundException(COMPILATION, compId));
    }

    @Override
    public List<Compilation> getCompilations(Integer from, Integer size, Boolean pinned) {
        Pageable pageable = PageRequest.of(from, size);
        return  pinned == null ? repository.findAll(pageable).getContent()
                : repository.findByPinned(pinned, pageable).getContent();
    }

    @Override
    public Compilation getCompilation(Long compId) {
        return get(compId);
    }
}
