package ru.practicum.comment.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentMapper;
import ru.practicum.comment.service.CommentService;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.CommentRespDto;
import ru.practicum.user.service.UserService;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin/comments")
public class AdminCommentServerController {
    private final CommentService service;
    private final UserService userService;

    @PatchMapping("/{id}")
    public CommentRespDto patch(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        Comment comment = service.patchByAdmin(id, commentDto);
        CommentRespDto resp = CommentMapper.toDto(comment);
        resp.setCommentator(userService.findInitiatorsByIds(Set.of(comment.getCommentator().getId())).get(0));
        return resp;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteByAdmin(id);
    }
}
