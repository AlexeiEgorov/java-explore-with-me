package ru.practicum.event.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EventPreviewResponseDto;
import ru.practicum.dto.EventResponseDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.model.EventStatus;

import java.time.LocalDateTime;

import static ru.practicum.Constants.FORMATTER;

@UtilityClass
public class EventMapper {
    public EventResponseDto toResponseDto(Event event) {
        String publishedOn = event.getPublishedOn() == null ? null : event.getPublishedOn().format(FORMATTER);
        return new EventResponseDto(
                event.getAnnotation(),
                null,
                event.getConfirmedRequests(),
                event.getCreatedOn().format(FORMATTER),
                event.getDescription(),
                event.getEventDate().format(FORMATTER),
                event.getId(),
                null,
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                publishedOn,
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                0L);
    }

    public EventPreviewResponseDto toPreviewDto(Event event) {
        return new EventPreviewResponseDto(
                event.getAnnotation(),
                null,
                event.getConfirmedRequests(),
                event.getEventDate().format(FORMATTER),
                event.getId(),
                null,
                event.getPaid(),
                event.getTitle(),
                0L);
    }

    public Event toEvent(EventDto eventDto) {
        Boolean paid = eventDto.getPaid() != null ? eventDto.getPaid() : false;
        Integer participantLimit = eventDto.getParticipantLimit() != null ? eventDto.getParticipantLimit() : 0;
        Boolean requestModeration = eventDto.getRequestModeration() != null ? eventDto.getRequestModeration() : true;
        return new Event(
                null,
                eventDto.getAnnotation(),
                null,
                0,
                null,
                eventDto.getDescription(),
                LocalDateTime.parse(eventDto.getEventDate(), FORMATTER),
                null,
                eventDto.getLocation(),
                paid,
                participantLimit,
                null,
                requestModeration,
                EventStatus.PENDING,
                eventDto.getTitle()
        );
    }
}
