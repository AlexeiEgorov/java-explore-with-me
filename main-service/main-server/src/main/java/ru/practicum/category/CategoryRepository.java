package ru.practicum.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.category.model.Category;
import ru.practicum.dto.EventCategoryDto;

import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select new ru.practicum.dto.EventCategoryDto(c.id, c.name) from Category c")
    List<EventCategoryDto> findCategoryDtosByIds(Set<Long> ids);
}
