package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotAllowedActionException extends RuntimeException {
    private String value;
}
