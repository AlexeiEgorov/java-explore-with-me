package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EntityNotFoundException extends RuntimeException {
    private final String entityClass;
    private final Long value;
}
