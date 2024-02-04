package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.EndpointHitStatMapper;
import ru.practicum.model.EndpointStat;
import ru.practicum.service.EndpointStatServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.FORMATTER;

@RestController
@AllArgsConstructor
public class StatsController {
    private final EndpointStatServiceImpl service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEndpointHitStat(@RequestBody EndpointHitStatDto endpointHitStatDto) {
        service.addEndpointHitStat(EndpointHitStatMapper.toEndpointHitStat(endpointHitStatDto));
    }

    @GetMapping("/stats")
    public List<EndpointStat> getStats(@RequestParam String start, @RequestParam String end,
                                       @RequestParam(required = false) String[] uris,
                                       @RequestParam Boolean unique) {
        LocalDateTime startD = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endD = LocalDateTime.parse(end, FORMATTER);
        return service.getStats(startD, endD, uris, unique);
    }
}
