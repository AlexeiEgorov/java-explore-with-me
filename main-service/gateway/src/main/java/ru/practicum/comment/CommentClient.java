package ru.practicum.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.dto.CommentDto;

import javax.validation.Valid;

@Service
public class CommentClient extends BaseClient {
    private static final String USER_API_PREFIX = "/users/%d/comments";
    private static final String ADMIN_API_PREFIX = "/admin/comments";
    private static final String PAGE_SUFFIX = "?from=%d&size=%d";
    private static final String EVENT_ID_PARAM = "?eventId=%d";

    @Autowired
    public CommentClient(@Value("${MAIN_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> add(Long userId, Long eventId, @Valid CommentDto commentDto) {
        return post(String.format(USER_API_PREFIX, userId) + String.format(EVENT_ID_PARAM,eventId), commentDto);
    }

    public ResponseEntity<Object> patchByAdmin(Long id, CommentDto commentDto) {
        return patch(ADMIN_API_PREFIX + "/" + id, commentDto);
    }

    public ResponseEntity<Object> deleteByAdmin(Long id) {
        return delete(ADMIN_API_PREFIX + "/" + id);
    }

    public ResponseEntity<Object> getAllUserComments(Long userId, Integer from, Integer size) {
        return get(String.format(USER_API_PREFIX, userId) + String.format(PAGE_SUFFIX, from, size));
    }

    public ResponseEntity<Object> delete(Long userId, Long id) {
        return delete(String.format(USER_API_PREFIX, userId) + "/" + id);
    }

    public ResponseEntity<Object> patch(Long userId, Long id, CommentDto commentDto) {
        return patch(String.format(USER_API_PREFIX, userId) + "/" + id, commentDto);
    }
}
