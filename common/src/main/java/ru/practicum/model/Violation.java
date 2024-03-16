package ru.practicum.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class Violation {
    private final String fieldName;
    private final String message;
}