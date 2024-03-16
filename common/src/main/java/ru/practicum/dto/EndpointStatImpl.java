package ru.practicum.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class EndpointStatImpl implements EndpointStat {
    private String app;
    private String uri;
    private Long hits;
}