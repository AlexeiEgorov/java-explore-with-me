package ru.practicum.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByCommentatorId(Long commentatorId, PageRequest pageRequest);

    List<Comment> findAllByEventId(Long eventId);
}
