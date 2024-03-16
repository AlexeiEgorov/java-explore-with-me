package ru.practicum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ConstraintViolationException extends RuntimeException {
    private final String value;

    public ConstraintViolationException(String message, String value) {
        super(message);
        this.value = value;
    }
}