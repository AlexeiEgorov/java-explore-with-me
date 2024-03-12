package ru.practicum.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.eventrequest.dto.ResponseEventRequestDto;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EventRequestsConfirmationResultDto {
    private List<ResponseEventRequestDto> confirmedRequests;
    private List<ResponseEventRequestDto> rejectedRequests;
}
