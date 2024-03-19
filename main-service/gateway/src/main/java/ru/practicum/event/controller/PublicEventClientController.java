package ru.practicum.event.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ViewsLoader;
import ru.practicum.dto.EndpointHitStatDto;
import ru.practicum.dto.EventFullResponseDto;
import ru.practicum.dto.EventPreviewResponseDto;
import ru.practicum.event.EventClient;
import ru.practicum.model.ConstraintViolationException;
import ru.practicum.model.NotAllowedActionException;
import ru.practicum.model.SortType;
import ru.practicum.stats.StatsClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.*;

@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/events")
public class PublicEventClientController {
    private final EventClient client;
    private final StatsClient statsClient;
    private final ObjectMapper objectMapper;
    private final ViewsLoader viewsLoader;
    @Value("${service.name}")
    private String serviceName;

    @GetMapping
    public ResponseEntity<Object> searchEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(defaultValue = "EVENT_DATE") SortType sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            HttpServletRequest request
    ) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new NotAllowedActionException("End time cannot go before start time");
            }
        }
        String requestTime = LocalDateTime.now().format(FORMATTER);
        statsClient.addEndpointHitStat(
                new EndpointHitStatDto(
                        serviceName,
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        requestTime));

        ResponseEntity<Object> resp = client.searchEventsForVisitor(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        if (resp.getStatusCode() == HttpStatus.OK) {
            List<EventPreviewResponseDto> eventDtos = objectMapper.convertValue(resp.getBody(),
                    new TypeReference<>() {});
            return ResponseEntity.ok(viewsLoader.loadViewsForEventDtos(eventDtos));
        }
        return resp;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEventForVisitor(@PathVariable Long id, HttpServletRequest request) {
        if (id < 1) {
            throw new ConstraintViolationException(POSITIVE_ID_CONS);
        }
        String requestTime = LocalDateTime.now().format(FORMATTER);

        statsClient.addEndpointHitStat(
                new EndpointHitStatDto(
                        serviceName,
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        requestTime));
        ResponseEntity<Object> resp = client.getEventForVisitor(id);
        if (resp.getStatusCode() == HttpStatus.OK) {
            EventFullResponseDto eventDto = objectMapper.convertValue(resp.getBody(), EventFullResponseDto.class);
            viewsLoader.loadViewsForEventDtos(List.of(eventDto));
            return ResponseEntity.ok(eventDto);
        }
        return resp;
    }
}
