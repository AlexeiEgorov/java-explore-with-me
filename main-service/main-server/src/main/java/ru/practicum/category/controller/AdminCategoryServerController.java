package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ResponseCategoryDto;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.service.CategoryService;
import ru.practicum.dto.CategoryDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryServerController {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCategoryDto add(@RequestBody CategoryDto categoryDto) {
        return CategoryMapper.toResponseDto(service.save(CategoryMapper.toCategory(categoryDto)));
    }

    @PatchMapping("/{catId}")
    public ResponseCategoryDto patch(@RequestBody CategoryDto categoryDto, @PathVariable Long catId) {
        return CategoryMapper.toResponseDto(service.patch(categoryDto, catId));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        service.delete(catId);
    }
}
