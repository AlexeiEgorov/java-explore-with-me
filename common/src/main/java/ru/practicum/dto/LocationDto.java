package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
public class LocationDto {
    @NotNull
    @Min(-90)
    @Max(90)
    private double lat;
    @NotNull
    @Min(-180)
    @Max(180)
    private double lon;
}
