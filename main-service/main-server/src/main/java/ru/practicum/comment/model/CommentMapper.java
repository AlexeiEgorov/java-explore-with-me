package ru.practicum.comment.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CommentRespDto;
import ru.practicum.dto.UserCommentRespDto;

import static ru.practicum.Constants.FORMATTER;

@UtilityClass
public class CommentMapper {
    public CommentRespDto toDto(Comment comment) {
        return new CommentRespDto(comment.getId(), comment.getText(), comment.getCreatedAt().format(FORMATTER),
                null, comment.getEvent().getId());
    }

    public UserCommentRespDto toUserCommentDto(Comment comment) {
        return new UserCommentRespDto(comment.getId(), comment.getText(), comment.getCreatedAt().format(FORMATTER),
                null);
    }
}
