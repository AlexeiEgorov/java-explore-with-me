package ru.practicum.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class CommentsCount {
    private Long eventId;
    private Long count;
}
