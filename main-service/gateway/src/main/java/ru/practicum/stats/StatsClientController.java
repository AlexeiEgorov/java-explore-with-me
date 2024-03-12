package ru.practicum.stats;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.model.NotAllowedActionException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constants.FORMATTER;

@Controller
@AllArgsConstructor
public class StatsClientController {
    private final StatsClient client;

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(end, FORMATTER);
        if (endTime.isBefore(startTime)) {
            throw new NotAllowedActionException(String.format("Start date cannot go before end date: %s, end: %s",
                    start, end));
        }
        try {
            String encodedStartTimeStr = URLEncoder.encode(start, StandardCharsets.UTF_8);
            String encodedEndTimeStr = URLEncoder.encode(end, StandardCharsets.UTF_8);
            return client.getStats(encodedStartTimeStr, encodedEndTimeStr, uris, unique);
        } catch (Exception e) {
            throw new NotAllowedActionException(String.format("Wrong time encoding start: %s, end: %s", start, end));
        }
    }
}
