package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.Location;
import ru.practicum.model.StateAction;

@Getter
@Setter
@AllArgsConstructor
public class EventPatchResponseDto {
    private final String annotation;
    private final Long category;
    private final String description;
    private final String eventDate;
    private final Location location;
    private final Boolean paid;
    private final Integer participantLimit;
    private final Boolean requestModeration;
    private StateAction stateAction;
    private final String title;
}
