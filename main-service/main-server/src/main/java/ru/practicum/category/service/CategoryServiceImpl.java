package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.EventCategoryDto;
import ru.practicum.exception.EntityNotFoundException;

import java.util.List;
import java.util.Set;

import static ru.practicum.LocalConstants.CATEGORY;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    @Transactional
    public Category save(Category category) {
        return repository.save(category);
    }

    @Override
    @Transactional
    public Category patch(CategoryDto categoryDto, Long id) {
        Category patched = get(id);
        patched.setName(categoryDto.getName());
        return repository.save(patched);
    }

    @Override
    public Page<Category> findCategories(Integer from, Integer size) {
        return repository.findAll(PageRequest.of(from / size, size));
    }

    @Override
    public Category get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(CATEGORY, id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        get(id);
        repository.deleteById(id);
    }

    @Override
    public List<EventCategoryDto> findCategoryDtosByIds(Set<Long> ids) {
        return repository.findCategoryDtosByIds(ids);
    }
}
