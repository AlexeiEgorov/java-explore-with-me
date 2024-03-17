package ru.practicum.comment.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentMapper;
import ru.practicum.comment.service.CommentService;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.CommentRespDto;
import ru.practicum.dto.EventForComment;
import ru.practicum.dto.UserCommentRespDto;
import ru.practicum.event.service.EventService;
import ru.practicum.user.service.UserService;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class UserCommentServerController {
    private final CommentService service;
    private final EventService eventService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentRespDto add(@PathVariable Long userId, @RequestParam Long eventId,
                              @RequestBody CommentDto commentDto
    ) {
        Comment comment = service.add(userId, eventId, commentDto);
        CommentRespDto resp = CommentMapper.toDto(comment);
        resp.setCommentator(userService.findInitiatorsByIds(Set.of(comment.getCommentator().getId())).get(0));
        return resp;
    }

    @PatchMapping("/{id}")
    public CommentRespDto patch(
            @PathVariable Long userId,
            @PathVariable Long id,
            @RequestBody CommentDto commentDto
    ) {
        Comment comment = service.patch(userId, id, commentDto);
        CommentRespDto resp = CommentMapper.toDto(comment);
        resp.setCommentator(userService.findInitiatorsByIds(Set.of(comment.getCommentator().getId())).get(0));
        return resp;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long id) {
        service.delete(userId, id);
    }

    @GetMapping
    public List<UserCommentRespDto> getAllUserComments(@PathVariable Long userId,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        List<Comment> comments = service.getAllUserComments(userId, from, size);
        Map<Long, EventForComment> eventDtos = new HashMap<>();
        for (Comment comment : comments) {
            eventDtos.put(comment.getEvent().getId(), null);
        }
        for (EventForComment eventDto : eventService.findAllEventForComment(eventDtos.keySet())) {
            eventDtos.put(eventDto.getId(), eventDto);
        }
        List<UserCommentRespDto> resp = new ArrayList<>();
        for (Comment comment : comments) {
            UserCommentRespDto commentRespDto = CommentMapper.toUserCommentDto(comment);
            commentRespDto.setEvent(eventDtos.get(comment.getEvent().getId()));
            resp.add(commentRespDto);
        }
        return resp;
    }
}