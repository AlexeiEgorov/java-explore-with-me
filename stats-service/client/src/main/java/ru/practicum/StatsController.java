package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static ru.practicum.Constants.FORMATTER;

@Controller
@RequiredArgsConstructor
@Validated
public class StatsController {
    private final StatsClient client;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEndpointHitStat(@RequestBody EndpointHitStatDto endpointHitStatDto) {
        client.addEndpointHitStat(endpointHitStatDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam String start, @RequestParam String end,
                                           @RequestParam(required = false) String[] uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        LocalDateTime startD = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endD = LocalDateTime.parse(end, FORMATTER);
        if (endD.isBefore(startD)) {
            throw new ConstraintViolationException("Start date cannot go before end date",
                    String.format("start: %s, end: %s", startD, endD));
        }
        return client.getStats(start, end, uris, unique);
    }
}
