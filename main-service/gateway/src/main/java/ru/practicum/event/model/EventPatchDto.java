package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.Location;
import ru.practicum.model.StateAction;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class EventPatchDto {
    @Pattern(regexp = "^(?!\\s*$).+", message = "Field must not consist only of whitespace characters")
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Pattern(regexp = "^(?!\\s*$).+", message = "Field must not consist only of whitespace characters")
    @Size(min = 3, max = 120)
    private String title;
}
