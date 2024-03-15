package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitStatDto;
import ru.practicum.dto.EndpointStat;
import ru.practicum.model.ConstraintViolationException;
import ru.practicum.model.EndpointHitStatMapper;
import ru.practicum.service.EndpointStatService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.DATE_TIME_FORMAT;

@RestController
@AllArgsConstructor
public class StatsController {
    private final EndpointStatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEndpointHitStat(@RequestBody EndpointHitStatDto endpointHitStatDto) {
        service.addEndpointHitStat(EndpointHitStatMapper.toEndpointHitStat(endpointHitStatDto));
    }

    @GetMapping("/stats")
    public List<EndpointStat> getStats(@RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false") Boolean unique) {
        try {
            if (end.isBefore(start)) {
                throw new ConstraintViolationException("Start date cannot go before end date",
                        String.format("start: %s, end: %s", start, end));
            }
            return service.getStats(start, end, uris, unique);
        } catch (Exception e) {
            throw new ConstraintViolationException("Wrong time encoding",
                    String.format("start: %s, end: %s", start, end));
        }
    }
}
