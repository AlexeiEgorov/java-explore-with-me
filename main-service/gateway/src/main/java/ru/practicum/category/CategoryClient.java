package ru.practicum.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.CategoryDto;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class CategoryClient extends BaseClient {
    private static final String ADMIN_API_PREFIX = "/admin/categories";
    private static final String USER_API_PREFIX = "/categories";
    private static final String GET_CATEGORIES_REQ = "/categories?from={from}&size={size}";

    @Autowired
    public CategoryClient(@Value("${MAIN_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }


    public ResponseEntity<Object> save(CategoryDto categoryDto) {
        return post(ADMIN_API_PREFIX, categoryDto);
    }

    public ResponseEntity<Object> patch(CategoryDto categoryDto, Long catId) {
        return patch(ADMIN_API_PREFIX + "/" + catId,  categoryDto);
    }

    public ResponseEntity<Object> delete(Long catId) {
        return delete(ADMIN_API_PREFIX + "/" + catId);
    }

    public ResponseEntity<Object> findCategories(Integer from, Integer size) {
        return get(GET_CATEGORIES_REQ, Map.of("from", from, "size", size));
    }

    public ResponseEntity<Object> get(Long catId) {
        return get(USER_API_PREFIX + "/" + catId);
    }
}
