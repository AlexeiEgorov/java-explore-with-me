package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
public class CategoryDto {
    @NotBlank(message = "Name can't be empty")
    @Size(max = 50, message = "Name can't have more than 50 symbols")
    private String name;
}