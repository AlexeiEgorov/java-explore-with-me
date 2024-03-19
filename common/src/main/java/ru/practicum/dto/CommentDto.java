package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class CommentDto {
    @NotBlank
    @Size(max = 2000)
    private String text;
}
