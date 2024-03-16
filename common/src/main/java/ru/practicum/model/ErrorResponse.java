package ru.practicum.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorResponse {
    private final String status;
    private final String reason;
    private final String message;
    private final String timestamp;
}