package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EndpointHitStatDto {
    private final String app;
    private final String uri;
    private final String ip;
    private final String timestamp;
}