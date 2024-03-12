package ru.practicum.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.dto.CompilationDto;

@Service
public class CompilationClient extends BaseClient {
    private static final String ADMIN_API_PREFIX = "/admin/compilations";
    private static final String PUBLIC_API_PREFIX = "/compilations";

    @Autowired
    public CompilationClient(@Value("${MAIN_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> add(CompilationDto requestDto) {
        return post(String.format(ADMIN_API_PREFIX), requestDto);
    }

    public ResponseEntity<Object> delete(Long compId) {
        return delete(ADMIN_API_PREFIX + "/" + compId);
    }

    public ResponseEntity<Object> patch(Long compId, CompilationDto dto) {
        return patch(ADMIN_API_PREFIX + "/" + compId, dto);
    }

    public ResponseEntity<Object> getCompilations(Integer from, Integer size, Boolean pinned) {
        StringBuilder uriBuilder = new StringBuilder(PUBLIC_API_PREFIX);
        uriBuilder.append("?from=").append(from);
        uriBuilder.append("&size=").append(size);
        if (pinned != null) {
            uriBuilder.append("&pinned=").append(pinned);
        }
        return get(uriBuilder.toString());
    }

    public ResponseEntity<Object> getCompilation(Long compId) {
        return get(PUBLIC_API_PREFIX + "/" + compId);
    }
}
