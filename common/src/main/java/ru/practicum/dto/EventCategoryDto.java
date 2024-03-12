package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EventCategoryDto {
    private final Long id;
    private final String name;
}
