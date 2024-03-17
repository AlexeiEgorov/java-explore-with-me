package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CommentRespDto {
    private final Long id;
    private final String text;
    private final String createdAt;
    private Initiator commentator;
    private final Long event;
}
