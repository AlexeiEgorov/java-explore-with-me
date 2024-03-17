package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.EventRespDto;
import ru.practicum.event.model.Event;
import ru.practicum.eventrequest.model.ConfirmedRequests;
import ru.practicum.eventrequest.service.EventRequestService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ConfirmedRequestsLoader {
    private final EventRequestService service;

    public <T extends EventRespDto> Set<T> loadForEventDtos(Set<T> events) {
        List<ConfirmedRequests> confirmedRequests = service.getConfirmedRequestsForEvents(events.stream()
                .map(EventRespDto::getId).collect(Collectors.toSet()));
        Map<Long, Long> confirmedCounts = confirmedRequests.stream()
                .collect(Collectors.toMap(ConfirmedRequests::getEventId, ConfirmedRequests::getCount));
        for (EventRespDto event : events) {
            event.setConfirmedRequests(confirmedCounts.get(event.getId()));
        }
        return events;
    }

    public <T extends EventRespDto> List<T> loadForEventDtos(List<T> events) {
        List<ConfirmedRequests> confirmedRequests = service.getConfirmedRequestsForEvents(events.stream()
                .map(EventRespDto::getId).collect(Collectors.toSet()));
        Map<Long, Long> confirmedCounts = confirmedRequests.stream()
                .collect(Collectors.toMap(ConfirmedRequests::getEventId, ConfirmedRequests::getCount));
        for (EventRespDto event : events) {
            event.setConfirmedRequests(confirmedCounts.get(event.getId()));
        }
        return events;
    }

    public Long getConfirmedCountForEvent(Event event) {
        return service.getConfirmedRequestsForEvents(Set.of(event.getId())).get(0).getCount();
    }
}
