package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class EventPreviewResponseDto implements EventRespDto {
    private final String annotation;
    private EventCategoryDto category;
    private Long confirmedRequests;
    private final String eventDate;
    private final Long id;
    private Initiator initiator;
    private final Boolean paid;
    private final String title;
    private Long views;
}
