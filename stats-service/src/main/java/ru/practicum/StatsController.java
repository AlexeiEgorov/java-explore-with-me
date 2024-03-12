package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitStatDto;
import ru.practicum.model.ConstraintViolationException;
import ru.practicum.model.EndpointHitStatMapper;
import ru.practicum.dto.EndpointStat;
import ru.practicum.service.EndpointStatService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.FORMATTER;

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
    public List<EndpointStat> getStats(@RequestParam String start,
                                       @RequestParam String end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false") Boolean unique) {
        try {
            String decodedStartTimeStr = URLDecoder.decode(start, StandardCharsets.UTF_8);
            String decodedEndTimeStr = URLDecoder.decode(end, StandardCharsets.UTF_8);
            LocalDateTime decodedStartTime = LocalDateTime.parse(decodedStartTimeStr, FORMATTER);
            LocalDateTime decodedEndTime = LocalDateTime.parse(decodedEndTimeStr, FORMATTER);
            if (decodedEndTime.isBefore(decodedStartTime)) {
                throw new ConstraintViolationException("Start date cannot go before end date",
                        String.format("start: %s, end: %s", decodedStartTime, decodedEndTime));
            }
            return service.getStats(decodedStartTime, decodedEndTime, uris, unique);
        } catch (Exception e) {
            throw new ConstraintViolationException("Wrong time encoding",
                    String.format("start: %s, end: %s", start, end));
        }
    }
}
