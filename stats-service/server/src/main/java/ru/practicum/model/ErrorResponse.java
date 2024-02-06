package ru.practicum.model;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
    private final String description;
}