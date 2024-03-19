package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.dto.*;
import ru.practicum.stats.StatsClient;

import java.time.LocalDateTime;
import java.util.*;

import static ru.practicum.Constants.FORMATTER;

@RequiredArgsConstructor
@Component
public class ViewsLoader {
    private final ObjectMapper objectMapper;
    private final StatsClient statsClient;
    private final LocalDateTime earliestDatePossible = LocalDateTime.parse("2024-03-15 12:00:00", FORMATTER);

    public <T extends EventRespDto> List<T> loadViewsForEventDtos(List<T> events) {
        if (events.isEmpty()) {
            return List.of();
        }
        Map<String, List<EventRespDto>> eventMap = new HashMap<>();
        LocalDateTime earliestEventDate = earliestDatePossible;
        if (events.iterator().next() instanceof EventResponseDto) {
            for (EventRespDto event : events) {
                EventResponseDto eventResponseDto = (EventResponseDto) event;
                if (eventResponseDto.getEventDate() != null) {
                    eventMap.computeIfAbsent("/events/" + event.getId().toString(), k ->
                            new ArrayList<>()).add(event);
                    LocalDateTime eventDate = LocalDateTime.parse(eventResponseDto.getEventDate(), FORMATTER);
                    if (eventDate.isBefore(earliestEventDate)) {
                        earliestEventDate = eventDate;
                    }
                }
            }
        } else {
            for (EventRespDto event : events) {
                eventMap.computeIfAbsent("/events/" + event.getId().toString(), k -> new ArrayList<>()).add(event);
            }
        }
        if (!eventMap.isEmpty()) {
            ResponseEntity<Object> statsResp = statsClient
                    .getStats(earliestEventDate,
                            LocalDateTime.now(),
                            new ArrayList<>(eventMap.keySet()),
                            true);
            List<EndpointStatImpl> statDtos = objectMapper.convertValue(statsResp.getBody(), new TypeReference<>() {
            });
            for (EndpointStat stat : statDtos) {
                for (EventRespDto respDto : eventMap.get(stat.getUri())) {
                    respDto.setViews(stat.getHits());
                }
            }
        }
        return events;
    }

    public <T extends EventRespDto> void loadViewsForEventDtos(Set<T> events) {
        if (events.isEmpty()) {
            return;
        }
        Map<String, List<EventRespDto>> eventMap = new HashMap<>();
        LocalDateTime earliestEventDate = earliestDatePossible;
        if (events.iterator().next() instanceof EventPreviewResponseDto) {
            for (EventRespDto event : events) {
                eventMap.computeIfAbsent("/events/" + event.getId().toString(), k -> new ArrayList<>()).add(event);
            }
        } else {
            for (EventRespDto event : events) {
                EventResponseDto eventResponseDto = (EventResponseDto) event;
                if (eventResponseDto.getEventDate() != null) {
                    eventMap.computeIfAbsent("/events/" + event.getId().toString(), k ->
                            new ArrayList<>()).add(event);
                    LocalDateTime eventDate = LocalDateTime.parse(eventResponseDto.getEventDate(), FORMATTER);
                    if (eventDate.isBefore(earliestEventDate)) {
                        earliestEventDate = eventDate;
                    }
                }
            }
        }
        if (!eventMap.isEmpty()) {
            ResponseEntity<Object> statsResp = statsClient
                    .getStats(earliestEventDate,
                            LocalDateTime.now(),
                            new ArrayList<>(eventMap.keySet()),
                            true);
            List<EndpointStatImpl> statDtos = objectMapper.convertValue(statsResp.getBody(), new TypeReference<>() {
            });
            for (EndpointStat stat : statDtos) {
                for (EventRespDto respDto : eventMap.get(stat.getUri())) {
                    respDto.setViews(stat.getHits());
                }
            }
        }
    }
}
