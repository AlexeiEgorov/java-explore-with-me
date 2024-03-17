package ru.practicum.comment.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.CommentRepository;
import ru.practicum.comment.model.Comment;
import ru.practicum.dto.CommentDto;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.model.EventStatus;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.LocalConstants.COMMENT;
import static ru.practicum.LocalConstants.EVENT;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Comment add(Long userId, Long eventId, CommentDto commentDto) {
        Event event = getEvent(eventId);
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new EntityNotFoundException(EVENT, eventId);
        }
        Comment comment = new Comment(null, commentDto.getText(), LocalDateTime.now(),
        new User(1L, null, null), event);
        return repository.save(comment);
    }

    @Override
    @Transactional
    public Comment patch(Long userId, Long id, CommentDto commentDto) {
        Comment comment = get(id);
        if (!comment.getCommentator().getId().equals(userId)) {
            throw new EntityNotFoundException(COMMENT, id);
        }
        comment.setText(commentDto.getText());
        return repository.save(comment);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long id) {
        Comment comment = get(id);
        if (!comment.getCommentator().getId().equals(userId)) {
            throw new EntityNotFoundException(COMMENT, id);
        }
        repository.delete(comment);
    }

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EVENT, id));
    }

    @Override
    public Comment get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(COMMENT, id));
    }

    @Override
    @Transactional
    public void deleteByAdmin(Long id) {
        repository.delete(get(id));
    }

    @Override
    @Transactional
    public Comment patchByAdmin(Long id, CommentDto commentDto) {
        Comment comment = get(id);
        comment.setText(commentDto.getText());
        return repository.save(comment);
    }

    @Override
    public List<Comment> getAllUserComments(Long userId, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from, size);
        return repository.findAllByCommentatorId(userId, pageRequest).getContent();
    }

    @Override
    public List<Comment> findAllByEventId(Long eventId) {
        return repository.findAllByEventId(eventId);
    }
}
