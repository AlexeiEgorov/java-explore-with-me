package ru.practicum.event.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.Location;
import ru.practicum.model.StateAction;

@ToString
@Getter
@Setter
public class EventPatchDto {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}
