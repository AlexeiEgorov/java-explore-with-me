package ru.practicum.category.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.dto.CategoryDto;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    @Transactional
    Category save(Category category);

    @Transactional
    Category patch(CategoryDto categoryDto, Long id);

    Page<Category> findCategories(Integer from, Integer size);

    @Transactional
    void delete(Long id);

    Category get(Long id);

    List<Category> findCategoriesByIds(Set<Long> ids);
}
