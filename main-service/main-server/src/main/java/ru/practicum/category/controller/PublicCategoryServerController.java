package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.ResponseCategoryDto;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.service.CategoryService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class PublicCategoryServerController {
    private final CategoryService service;

    @GetMapping
    public Collection<ResponseCategoryDto> findCategories(@RequestParam Integer from, @RequestParam Integer size) {
        return service.findCategories(from, size).stream().map(CategoryMapper::toResponseDto).collect(Collectors.toList());
    }

    @GetMapping("/{catId}")
    public ResponseCategoryDto get(@PathVariable Long catId) {
        return CategoryMapper.toResponseDto(service.get(catId));
    }
}
