package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ResponseUserDto {
    private final Long id;
    private final String name;
    private final String email;
}