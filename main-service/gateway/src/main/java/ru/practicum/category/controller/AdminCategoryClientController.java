package ru.practicum.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.CategoryClient;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.ResponseCategoryDto;
import ru.practicum.model.ConstraintViolationException;

import javax.validation.Valid;

import static ru.practicum.Constants.POSITIVE_ID_CONS;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryClientController {
    private final CategoryClient client;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid CategoryDto categoryDto) {
        ResponseEntity<Object> responseEntity = client.save(categoryDto);
        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            ResponseCategoryDto addedUserDto = objectMapper.convertValue(responseEntity.getBody(),
                    ResponseCategoryDto.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedUserDto);
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        }
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<Object> patch(@RequestBody @Valid CategoryDto categoryDto,
                                        @PathVariable Long catId) {
        if (catId < 1) {
            throw new ConstraintViolationException(POSITIVE_ID_CONS);
        }
        return client.patch(categoryDto, catId);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> delete(@PathVariable Long catId) {
        if (catId < 1) {
            throw new ConstraintViolationException(POSITIVE_ID_CONS);
        }
        return client.delete(catId);
    }
}
