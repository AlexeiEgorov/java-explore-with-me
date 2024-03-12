package ru.practicum.category.model;

import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.ResponseCategoryDto;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.EventCategoryDto;

@UtilityClass
public class CategoryMapper {
    public ResponseCategoryDto toResponseDto(Category category) {
        return new ResponseCategoryDto(category.getId(), category.getName());
    }

    public Category toCategory(CategoryDto categoryDto) {
        return new Category(null, categoryDto.getName());
    }

    public EventCategoryDto toEventCategoryDto(Category category) {
        return  new EventCategoryDto(category.getId(), category.getName());
    }
}
