package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventRequestsConfirmationDto {
    private List<Long> requestIds;
    private EventRequestConfirmationStatus status;
}
