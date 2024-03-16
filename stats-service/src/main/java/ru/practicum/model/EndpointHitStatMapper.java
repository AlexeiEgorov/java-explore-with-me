package ru.practicum.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointHitStatDto;

import java.time.LocalDateTime;

import static ru.practicum.Constants.FORMATTER;

@UtilityClass
public class EndpointHitStatMapper {
    public EndpointHitStat toEndpointHitStat(EndpointHitStatDto endpointHitStatDto) {
        return new EndpointHitStat(null, endpointHitStatDto.getApp(), endpointHitStatDto.getUri(),
                endpointHitStatDto.getIp(), LocalDateTime.parse(endpointHitStatDto.getTimestamp(), FORMATTER));
    }
}
