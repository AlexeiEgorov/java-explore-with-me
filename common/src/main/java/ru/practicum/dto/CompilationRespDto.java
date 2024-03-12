package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class CompilationRespDto {
    private final Long id;
    private final String title;
    private final Boolean pinned;
    private List<EventPreviewResponseDto> events;
}
