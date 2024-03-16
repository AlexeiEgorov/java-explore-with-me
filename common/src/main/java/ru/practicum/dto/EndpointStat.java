package ru.practicum.dto;

public interface EndpointStat {
    String getApp();

    String getUri();

    Long getHits();
}