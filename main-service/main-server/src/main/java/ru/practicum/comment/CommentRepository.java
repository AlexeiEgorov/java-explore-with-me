package ru.practicum.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentsCount;

import java.util.List;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByCommentatorId(Long commentatorId, PageRequest pageRequest);

    List<Comment> findAllByEventId(Long eventId);

    @Query("SELECT c.event.id as eventId, COUNT(c) as count FROM Comment c " +
            "WHERE c.event.id IN ?1 GROUP BY c.event.id")
    List<CommentsCount> findCommentsCountByEventIds(Set<Long> eventIds);
}
