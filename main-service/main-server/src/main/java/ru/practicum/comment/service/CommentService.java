package ru.practicum.comment.service;

import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentsCount;
import ru.practicum.dto.CommentDto;
import ru.practicum.event.model.Event;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

public interface CommentService {
    Comment add(Long userId, Long eventId, CommentDto commentDto);

    Comment patch(Long userId, Long id, CommentDto commentDto);

    void delete(Long userId, Long id);

    Event getEvent(Long id);

    Comment get(Long id);

    void deleteByAdmin(Long id);

    Comment patchByAdmin(Long id, CommentDto commentDto);

    List<Comment> getAllUserComments(Long userId, @PositiveOrZero Integer from, @Positive Integer size);

    List<Comment> findAllByEventId(Long eventId);

    List<CommentsCount> getCommentsCountByEventIds(Set<Long> eventIds);
}
