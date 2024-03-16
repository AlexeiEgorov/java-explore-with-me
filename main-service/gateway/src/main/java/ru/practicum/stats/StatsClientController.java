package ru.practicum.stats;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.model.NotAllowedActionException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.DATE_TIME_FORMAT;

@Controller
@AllArgsConstructor
public class StatsClientController {
    private final StatsClient client;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam
                                           @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
                                           @RequestParam
                                           @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        if (end.isBefore(start)) {
            throw new NotAllowedActionException(String.format("Start date cannot go before end date: %s, end: %s",
                    start, end));
        }
        return client.getStats(start, end, uris, unique);
    }
}
