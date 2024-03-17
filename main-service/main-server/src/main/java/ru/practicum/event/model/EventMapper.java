package ru.practicum.event.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.*;
import ru.practicum.model.EventStatus;

import java.util.ArrayList;

import static ru.practicum.Constants.FORMATTER;

@UtilityClass
public class EventMapper {
    public EventResponseDto toResponseDto(Event event) {
        String publishedOn = event.getPublishedOn() == null ? null : event.getPublishedOn().format(FORMATTER);
        return new EventResponseDto(
                event.getAnnotation(),
                null,
                0L,
                event.getCreatedOn().format(FORMATTER),
                event.getDescription(),
                event.getEventDate().format(FORMATTER),
                event.getId(),
                null,
                toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                publishedOn,
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                0L);
    }

    public EventFullResponseDto toFullResponseDto(Event event) {
        String publishedOn = event.getPublishedOn() == null ? null : event.getPublishedOn().format(FORMATTER);
        return new EventFullResponseDto(
                event.getAnnotation(),
                null,
                0L,
                event.getCreatedOn().format(FORMATTER),
                event.getDescription(),
                event.getEventDate().format(FORMATTER),
                event.getId(),
                null,
                toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                publishedOn,
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                0L,
                new ArrayList<>());
    }

    public EventPreviewResponseDto toPreviewDto(Event event) {
        return new EventPreviewResponseDto(
                event.getAnnotation(),
                null,
                0L,
                event.getEventDate().format(FORMATTER),
                event.getId(),
                null,
                event.getPaid(),
                event.getTitle(),
                0L);
    }

    public Event toEvent(EventDto eventDto) {
        Boolean paid = eventDto.getPaid() != null ? eventDto.getPaid() : false;
        Long participantLimit = eventDto.getParticipantLimit() != null ? eventDto.getParticipantLimit() : 0L;
        Boolean requestModeration = eventDto.getRequestModeration() != null ? eventDto.getRequestModeration() : true;
        return new Event(
                null,
                eventDto.getAnnotation(),
                null,
                null,
                eventDto.getDescription(),
                eventDto.getEventDate(),
                null,
                toLocation(eventDto.getLocation()),
                paid,
                participantLimit,
                null,
                requestModeration,
                EventStatus.PENDING,
                eventDto.getTitle()
        );
    }

    public Location toLocation(LocationDto locationDto) {
        return new Location(locationDto.getLat(), locationDto.getLon());
    }

    public LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }
}
