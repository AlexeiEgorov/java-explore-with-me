package ru.practicum.compilation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.CompilationClient;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CompilationRespDto;
import ru.practicum.ViewsLoader;
import ru.practicum.model.Marker;

import javax.validation.constraints.Positive;

@Controller
@AllArgsConstructor
@Validated
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationClientController {
    private final CompilationClient client;
    private final ObjectMapper objectMapper;
    private final ViewsLoader viewsLoader;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Validated(Marker.Create.class) CompilationDto requestDto) {
        ResponseEntity<Object> resp = client.add(requestDto);
        if (resp.getStatusCode() == HttpStatus.OK) {
            CompilationRespDto dto = objectMapper.convertValue(resp.getBody(), CompilationRespDto.class);
            viewsLoader.loadViewsForEventDtos(dto.getEvents());
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        }
        return resp;
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long compId) {
        return client.delete(compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<Object> patch(@PathVariable @Positive Long compId,
                             @RequestBody @Validated(Marker.Update.class) CompilationDto compilationDto) {
        ResponseEntity<Object> resp = client.patch(compId, compilationDto);
        if (resp.getStatusCode() == HttpStatus.OK) {
            CompilationRespDto dto = objectMapper.convertValue(resp.getBody(), CompilationRespDto.class);
            viewsLoader.loadViewsForEventDtos(dto.getEvents());
            return ResponseEntity.ok(dto);
        }
        return resp;
    }

}
