package ru.practicum.service;

import ru.practicum.dto.EndpointStat;
import ru.practicum.model.EndpointHitStat;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointStatService {
    void addEndpointHitStat(EndpointHitStat endpointHitStat);

    List<EndpointStat> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
