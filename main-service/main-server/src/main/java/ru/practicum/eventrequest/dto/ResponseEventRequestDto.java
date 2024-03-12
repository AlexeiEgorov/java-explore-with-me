package ru.practicum.eventrequest.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.practicum.dto.EventRequestStatus;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ResponseEventRequestDto {
    private final Long id;
    private final String created;
    private final Long event;
    private final Long requester;
    private final EventRequestStatus status;
}