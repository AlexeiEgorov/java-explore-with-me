package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class EventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    @DateTimeFormat
    private String eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull
    @Size(min = 3, max = 120)
    private String title;
}
