package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.Location;
import ru.practicum.model.EventStatus;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EventResponseDto implements EventRespDto {
    private final String annotation;
    private EventCategoryDto category;
    private final Integer confirmedRequests;
    private final String createdOn;
    private final String description;
    private final String eventDate;
    private final Long id;
    private Initiator initiator;
    private final Location location;
    private final Boolean paid;
    private final Integer participantLimit;
    private final String publishedOn;
    private final Boolean requestModeration;
    private final EventStatus state;
    private final String title;
    private Long views;
}
