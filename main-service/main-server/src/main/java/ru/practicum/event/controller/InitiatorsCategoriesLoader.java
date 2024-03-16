package ru.practicum.event.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.service.CategoryService;
import ru.practicum.dto.EventCategoryDto;
import ru.practicum.dto.EventPreviewResponseDto;
import ru.practicum.dto.EventResponseDto;
import ru.practicum.dto.Initiator;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.user.service.UserService;

import java.util.*;

@Component
@AllArgsConstructor
public class InitiatorsCategoriesLoader {
    private final UserService userService;
    private final CategoryService categoryService;

    public Map<Long, EventCategoryDto> fetchCategories(Set<Event> events) {
        Map<Long, EventCategoryDto> categoriesMap = new HashMap<>();
        for (Event event : events) {
            categoriesMap.put(event.getCategory().getId(), null);
        }
        categoryService.findCategoriesByIds(categoriesMap.keySet())
                .forEach(category -> categoriesMap.put(category.getId(), CategoryMapper.toEventCategoryDto(category)));
        return categoriesMap;
    }

    public Map<Long, Initiator> fetchInitiators(Set<Event> events) {
        Map<Long, Initiator> initiatorsMap = new HashMap<>();
        for (Event event: events) {
            initiatorsMap.put(event.getInitiator().getId(), null);
        }
        userService.findInitiatorsByIds(initiatorsMap.keySet())
                .forEach(initiator -> initiatorsMap.put(initiator.getId(), initiator));
        return initiatorsMap;
    }

    public Map<Long, EventCategoryDto> fetchCategories(List<Event> events) {
        Map<Long, EventCategoryDto> categoriesMap = new HashMap<>();
        for (Event event : events) {
            categoriesMap.put(event.getCategory().getId(), null);
        }
        categoryService.findCategoriesByIds(categoriesMap.keySet())
                .forEach(category -> categoriesMap.put(category.getId(), CategoryMapper.toEventCategoryDto(category)));
        return categoriesMap;
    }

    public Map<Long, Initiator> fetchInitiators(List<Event> events) {
        Map<Long, Initiator> initiatorsMap = new HashMap<>();
        for (Event event: events) {
            initiatorsMap.put(event.getInitiator().getId(), null);
        }
        userService.findInitiatorsByIds(initiatorsMap.keySet())
                .forEach(initiator -> initiatorsMap.put(initiator.getId(), initiator));
        return initiatorsMap;
    }

    public List<EventResponseDto> loadFullResponseDtos(List<Event> events) {
        if (events.isEmpty()) {
            return List.of();
        }
        Map<Long, EventCategoryDto> categories = fetchCategories(events);
        Map<Long, Initiator> initiators = fetchInitiators(events);
        List<EventResponseDto> resp = new ArrayList<>();
        for (Event event : events) {
            EventResponseDto dto = EventMapper.toResponseDto(event);
            dto.setInitiator(initiators.get(event.getInitiator().getId()));
            dto.setCategory(categories.get(event.getCategory().getId()));
            dto.setViews(1L);
            resp.add(dto);
        }
        return resp;
    }

    public List<EventPreviewResponseDto> loadPreviewResponseDtos(List<Event> events) {
        if (events.isEmpty()) {
            return List.of();
        }
        Map<Long, EventCategoryDto> categories = fetchCategories(events);
        Map<Long, Initiator> initiators = fetchInitiators(events);
        List<EventPreviewResponseDto> resp = new ArrayList<>();
        for (Event event : events) {
            EventPreviewResponseDto dto = EventMapper.toPreviewDto(event);
            dto.setInitiator(initiators.get(event.getInitiator().getId()));
            dto.setCategory(categories.get(event.getCategory().getId()));
            dto.setViews(1L);
            resp.add(dto);
        }
        return resp;
    }

    public Set<EventPreviewResponseDto> loadPreviewResponseDtos(Set<Event> events) {
        if (events.isEmpty()) {
            return Set.of();
        }
        Map<Long, EventCategoryDto> categories = fetchCategories(events);
        Map<Long, Initiator> initiators = fetchInitiators(events);
        Set<EventPreviewResponseDto> resp = new LinkedHashSet<>();
        for (Event event : events) {
            EventPreviewResponseDto dto = EventMapper.toPreviewDto(event);
            dto.setInitiator(initiators.get(event.getInitiator().getId()));
            dto.setCategory(categories.get(event.getCategory().getId()));
            dto.setViews(1L);
            resp.add(dto);
        }
        return resp;
    }
}
