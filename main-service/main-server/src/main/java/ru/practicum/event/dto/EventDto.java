package ru.practicum.event.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.Location;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EventDto {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
