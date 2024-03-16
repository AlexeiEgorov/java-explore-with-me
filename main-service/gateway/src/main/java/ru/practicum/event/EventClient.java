package ru.practicum.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.dto.EventRequestsConfirmationDto;
import ru.practicum.dto.EventDto;
import ru.practicum.dto.EventPatchDto;
import ru.practicum.model.EventStatus;
import ru.practicum.model.SortType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.Constants.FORMATTER;

@Service
public class EventClient extends BaseClient {
    private static final String USER_API_PREFIX = "/users/%s/events";
    private static final String REQUESTS_SUFFIX = "/requests";
    private static final String FROM_SIZE_PARAMS = "?from={from}&size={size}";
    private static final String ADMIN_API_PREFIX = "/admin/events";
    private static final String PUBLIC_API_PREFIX = "/events";

    @Autowired
    public EventClient(@Value("${MAIN_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> add(Long userId, EventDto eventDto) {
        return post(String.format(USER_API_PREFIX, userId), eventDto);
    }

    public ResponseEntity<Object> getUserEvents(Long userId, Integer from, Integer size) {
        return get(String.format(USER_API_PREFIX, userId) + FROM_SIZE_PARAMS,
                Map.of("from", from, "size", size));
    }

    public ResponseEntity<Object> getUserEvent(Long userId, Long id) {
        return get(String.format(USER_API_PREFIX, userId) + "/" + id);
    }

    public ResponseEntity<Object> patch(Long userId, Long id, EventPatchDto eventPatchDto) {
        return patch(String.format(USER_API_PREFIX, userId) + "/" + id, eventPatchDto);
    }

    public ResponseEntity<Object> getEventRequests(Long userId, Long id) {
        return get(String.format(USER_API_PREFIX, userId) + "/" + id + REQUESTS_SUFFIX);
    }

    public ResponseEntity<Object> updateEventRequestsStatuses(
            Long userId,
            Long id,
            EventRequestsConfirmationDto eventRequestsConfirmationDto) {
        return patch(String.format(USER_API_PREFIX, userId) + "/" + id + REQUESTS_SUFFIX,
                eventRequestsConfirmationDto);
    }

    public ResponseEntity<Object> searchEventsForAdmin(
            List<Long> users,
            List<EventStatus> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size) {
        StringBuilder uriBuilder = new StringBuilder(ADMIN_API_PREFIX);
        uriBuilder.append("?from=").append(from);
        uriBuilder.append("&size=").append(size);

        if (users != null && !users.isEmpty()) {
            uriBuilder.append("&users=").append(users.stream().map(Object::toString)
                    .collect(Collectors.joining(",")));
        }
        if (states != null && !states.isEmpty()) {
            uriBuilder.append("&states=").append(states.stream().map(Object::toString)
                    .collect(Collectors.joining(",")));
        }
        if (categories != null && !categories.isEmpty()) {
            uriBuilder.append("&categories=").append(categories.stream().map(Object::toString)
                    .collect(Collectors.joining(",")));
        }
        if (rangeStart != null) {
            uriBuilder.append("&rangeStart=").append(rangeStart.format(FORMATTER));
        }
        if (rangeEnd != null) {
            uriBuilder.append("&rangeEnd=").append(rangeEnd.format(FORMATTER));
        }

        return get(uriBuilder.toString());
    }

    public ResponseEntity<Object> reviewEvent(Long id, EventPatchDto eventPatchDto) {
        return patch(ADMIN_API_PREFIX + "/" + id, eventPatchDto);
    }

    public ResponseEntity<Object> searchEventsForVisitor(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            SortType sort,
            Integer from,
            Integer size) {
        StringBuilder uriBuilder = new StringBuilder(PUBLIC_API_PREFIX);
        uriBuilder.append("?from=").append(from);
        uriBuilder.append("&size=").append(size);

        if (text != null) {
            uriBuilder.append("&text=").append(text);
        }
        if (categories != null && !categories.isEmpty()) {
            uriBuilder.append("&categories=").append(categories.stream().map(Object::toString)
                    .collect(Collectors.joining(",")));
        }
        if (paid != null) {
            uriBuilder.append("&paid=").append(paid);
        }
        if (rangeStart != null) {
            uriBuilder.append("&rangeStart=").append(rangeStart.format(FORMATTER));
        }
        if (rangeEnd != null) {
            uriBuilder.append("&rangeEnd=").append(rangeEnd.format(FORMATTER));
        }
        if (onlyAvailable != null) {
            uriBuilder.append("&onlyAvailable=").append(onlyAvailable);
        }
        if (sort != null) {
            uriBuilder.append("&sort=").append(sort);
        }

        return get(uriBuilder.toString());
    }

    public ResponseEntity<Object> getEventForVisitor(Long id) {
        return get(PUBLIC_API_PREFIX + "/" + id);
    }
}
