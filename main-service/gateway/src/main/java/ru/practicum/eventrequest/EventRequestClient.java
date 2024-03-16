package ru.practicum.eventrequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class EventRequestClient extends BaseClient {
    private static final String API_PREFIX = "/users/%d/requests";
    private static final String CANCEL_PREFIX = "/users/%d/requests/%d/cancel";
    private static final String ADD_PREFIX = "/users/%d/requests?eventId=%d";

    @Autowired
    public EventRequestClient(@Value("${MAIN_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> add(Long userId, Long eventId) {
        return post(String.format(ADD_PREFIX, userId, eventId), Map.of("eventId", eventId));
    }

    public ResponseEntity<Object> getUserRequests(Long userId) {
        return get(String.format(API_PREFIX, userId));
    }

    public ResponseEntity<Object> cancel(Long userId, Long requestId) {
        return patch(String.format(CANCEL_PREFIX, userId, requestId));
    }
}
