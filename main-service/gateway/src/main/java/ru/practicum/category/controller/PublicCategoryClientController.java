package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.category.CategoryClient;
import ru.practicum.model.ConstraintViolationException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.Constants.POSITIVE_ID_CONS;

@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/categories")
public class PublicCategoryClientController {
    private final CategoryClient client;

    @GetMapping
    public ResponseEntity<Object> findCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size) {
        return client.findCategories(from, size);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> get(@PathVariable Long catId) {
        if (catId < 1) {
            throw new ConstraintViolationException(POSITIVE_ID_CONS);
        }
        return client.get(catId);
    }
}
