package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.EventStatus;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EventFullResponseDto implements EventRespDto {
    private final String annotation;
    private EventCategoryDto category;
    private Long confirmedRequests;
    private final String createdOn;
    private final String description;
    private final String eventDate;
    private final Long id;
    private Initiator initiator;
    private final LocationDto location;
    private final Boolean paid;
    private final Long participantLimit;
    private final String publishedOn;
    private final Boolean requestModeration;
    private final EventStatus state;
    private final String title;
    private Long views;
    private List<CommentRespDto> comments;
}
