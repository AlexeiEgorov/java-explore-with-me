package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserCommentRespDto {
    private final Long id;
    private final String text;
    private final String createdAt;
    private EventForComment event;
    private final String lastUpdated;
}
