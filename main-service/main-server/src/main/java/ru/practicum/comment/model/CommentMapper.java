package ru.practicum.comment.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CommentRespDto;
import ru.practicum.dto.UserCommentRespDto;

import static ru.practicum.Constants.FORMATTER;

@UtilityClass
public class CommentMapper {
    public CommentRespDto toDto(Comment comment) {
        String lastUpdated = comment.getLastUpdated() == null ? null : comment.getLastUpdated().format(FORMATTER);
        return new CommentRespDto(comment.getId(), comment.getText(), comment.getCreatedAt().format(FORMATTER),
                null, comment.getEvent().getId(), lastUpdated);
    }

    public UserCommentRespDto toUserCommentDto(Comment comment) {
        String lastUpdated = comment.getLastUpdated() == null ? null : comment.getLastUpdated().format(FORMATTER);
        return new UserCommentRespDto(comment.getId(), comment.getText(), comment.getCreatedAt().format(FORMATTER),
                null, lastUpdated);
    }
}
