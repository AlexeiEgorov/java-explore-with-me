package ru.practicum.eventrequest.model;

import lombok.experimental.UtilityClass;
import ru.practicum.eventrequest.dto.ResponseEventRequestDto;

import static ru.practicum.Constants.FORMATTER;

@UtilityClass
public class EventRequestMapper {
    public ResponseEventRequestDto toResponseDto(EventRequest eventRequest) {
        return new ResponseEventRequestDto(eventRequest.getId(),
                eventRequest.getCreated().format(FORMATTER),
                eventRequest.getEvent().getId(),
                eventRequest.getRequester().getId(),
                eventRequest.getStatus());
    }
}
