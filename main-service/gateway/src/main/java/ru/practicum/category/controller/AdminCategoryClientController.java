package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.CategoryClient;
import ru.practicum.dto.CategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryClientController {
    private final CategoryClient client;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid CategoryDto categoryDto) {
        return client.save(categoryDto);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<Object> patch(@RequestBody @Valid CategoryDto categoryDto,
                                        @PathVariable @Positive Long catId) {
        return client.patch(categoryDto, catId);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long catId) {
        return client.delete(catId);
    }
}