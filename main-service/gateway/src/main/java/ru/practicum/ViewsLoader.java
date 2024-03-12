package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.dto.*;
import ru.practicum.stats.StatsClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.Constants.FORMATTER;

@RequiredArgsConstructor
@Component
public class ViewsLoader {
    private final ObjectMapper objectMapper;
    private final StatsClient statsClient;

    public <T extends EventRespDto> List<T> loadViewsForEventDtos(List<T> events) {
        Map<String, List<EventRespDto>> eventMap = new HashMap<>();
        for (EventRespDto event : events) {
            eventMap.computeIfAbsent("/events/" + event.getId().toString(), k -> new ArrayList<>()).add(event);
        }
        ResponseEntity<Object> statsResp = statsClient
                .getStats("2024-03-06 10:00:00",
                        LocalDateTime.now().format(FORMATTER),
                        new ArrayList<>(eventMap.keySet()),
                        true);
        List<EndpointStatImpl> statDtos = objectMapper.convertValue(statsResp.getBody(), new TypeReference<>() {});
        for (EndpointStat stat : statDtos) {
            for (EventRespDto respDto : eventMap.get(stat.getUri())) {
                respDto.setViews(stat.getHits());
            }
        }
        return events;
    }
}
