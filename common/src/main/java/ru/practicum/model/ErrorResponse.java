package ru.practicum.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ErrorResponse {
    private List<Violation> errors;
    private final String status;
    private final String reason;
    private final String message;
    private final String timestamp;
}