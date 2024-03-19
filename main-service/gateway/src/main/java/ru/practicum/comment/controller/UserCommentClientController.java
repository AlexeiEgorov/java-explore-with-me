package ru.practicum.comment.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.CommentClient;
import ru.practicum.dto.CommentDto;
import ru.practicum.model.ConstraintViolationException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.Constants.POSITIVE_ID_CONS;

@Controller
@Validated
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class UserCommentClientController {
    private final CommentClient client;

    @PostMapping
    public ResponseEntity<Object> add(
            @PathVariable Long userId,
            @RequestParam Long eventId,
            @RequestBody @Valid CommentDto commentDto
    ) {
        if (eventId < 1) {
            throw new ConstraintViolationException(POSITIVE_ID_CONS);
        }
        return client.add(userId, eventId, commentDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(
            @PathVariable Long userId,
            @PathVariable Long id,
            @RequestBody @Valid CommentDto commentDto
    ) {
        if (id < 1) {
            throw new ConstraintViolationException(POSITIVE_ID_CONS);
        }
        return client.patch(userId, id, commentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long userId, @PathVariable Long id) {
        if (id < 1) {
            throw new ConstraintViolationException(POSITIVE_ID_CONS);
        }
        return client.delete(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserComments(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        return client.getAllUserComments(userId, from, size);
    }
}