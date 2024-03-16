package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/admin/users";
    private static final String GET_USERS_REQ = "?from={from}&size={size}";
    private static final String GET_USERS_REQ_WITH_IDS = "?ids={ids}&from={from}&size={size}";

    @Autowired
    public UserClient(@Value("${MAIN_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> findUsers(List<Long> ids, Integer from, Integer size) {
        Map<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("size", size);
        if (ids == null) {
            return get(GET_USERS_REQ, params);
        } else {
            params.put("ids", ids.stream().map(String::valueOf).collect(Collectors.joining(",")));
            return get(GET_USERS_REQ_WITH_IDS, params);
        }
    }

    public ResponseEntity<Object> add(UserDto userDto) {
        return post("", userDto);
    }

    public ResponseEntity<Object> delete(Long userId) {
        return delete("/" + userId);
    }

}
