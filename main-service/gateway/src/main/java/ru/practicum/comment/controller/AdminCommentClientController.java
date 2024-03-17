package ru.practicum.comment.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.CommentClient;
import ru.practicum.dto.CommentDto;
import ru.practicum.model.ConstraintViolationException;

import javax.validation.Valid;

import static ru.practicum.Constants.POSITIVE_ID_CONS;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/admin/comments")
public class AdminCommentClientController {
    private final CommentClient client;

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(@PathVariable Long id, @RequestBody @Valid CommentDto commentDto) {
        if (id < 1) {
            throw new ConstraintViolationException(POSITIVE_ID_CONS);
        }
        return client.patchByAdmin(id, commentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        if (id < 1) {
            throw new ConstraintViolationException(POSITIVE_ID_CONS);
        }
        return client.deleteByAdmin(id);
    }
}
