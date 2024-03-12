package ru.practicum.dto;

public interface EventRespDto {
    Long getId();

    void setViews(Long count);

    void setCategory(EventCategoryDto eventCategoryDto);

    void setInitiator(Initiator initiator);

    EventCategoryDto getCategory();

    Initiator getInitiator();
}
