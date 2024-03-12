package ru.practicum.compilation.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ViewsLoader;
import ru.practicum.compilation.CompilationClient;
import ru.practicum.dto.CompilationRespDto;
import ru.practicum.dto.EventPreviewResponseDto;
import ru.practicum.model.ConstraintViolationException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@Validated
@RequestMapping(path = "/compilations")
public class PublicCompilationClientController {
    private final CompilationClient client;
    private final ObjectMapper objectMapper;
    private final ViewsLoader viewsLoader;

    @GetMapping
    public ResponseEntity<Object> getCompilations(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            @RequestParam(required = false) Boolean pinned
    ) {
        ResponseEntity<Object> resp = client.getCompilations(from, size, pinned);
        if (resp.getStatusCode() == HttpStatus.OK) {
            List<CompilationRespDto> dtos = objectMapper.convertValue(resp.getBody(), new TypeReference<>() {});
            List<EventPreviewResponseDto> eventDtos = new ArrayList<>();
            for (CompilationRespDto compilationRespDto : dtos) {
                eventDtos.addAll(compilationRespDto.getEvents());
            }
            viewsLoader.loadViewsForEventDtos(eventDtos);
            return ResponseEntity.ok(dtos);
        }
        return resp;
    }

    @GetMapping("/{compId}")
    public ResponseEntity<Object> getCompilation(@PathVariable Long compId) {
        if (compId < 1) {
            throw new ConstraintViolationException("Id should be positive");
        }
        ResponseEntity<Object> resp = client.getCompilation(compId);
        if (resp.getStatusCode() == HttpStatus.OK) {
            CompilationRespDto dto = objectMapper.convertValue(resp.getBody(), CompilationRespDto.class);
            viewsLoader.loadViewsForEventDtos(dto.getEvents());
            return ResponseEntity.ok(dto);
        }
        return resp;
    }

}
