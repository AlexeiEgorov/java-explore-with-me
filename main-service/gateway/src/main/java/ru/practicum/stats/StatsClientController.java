package ru.practicum.stats;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.model.ConstraintViolationException;

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
            throw new ConstraintViolationException("Start date cannot go before end date",
                    String.format("start: %s, end: %s", start, end));
        }
        try {
            String encodedStartTimeStr = URLEncoder.encode(start, StandardCharsets.UTF_8);
            String encodedEndTimeStr = URLEncoder.encode(end, StandardCharsets.UTF_8);
            return client.getStats(encodedStartTimeStr, encodedEndTimeStr, uris, unique);
        } catch (Exception e) {
            throw new ConstraintViolationException("Wrong time encoding",
                    String.format("start: %s, end: %s", start, end));
        }
    }
}
