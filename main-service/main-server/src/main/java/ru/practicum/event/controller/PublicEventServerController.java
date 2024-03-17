package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ConfirmedRequestsLoader;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentMapper;
import ru.practicum.comment.service.CommentService;
import ru.practicum.dto.CommentRespDto;
import ru.practicum.dto.EventFullResponseDto;
import ru.practicum.dto.EventPreviewResponseDto;
import ru.practicum.dto.Initiator;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.service.EventService;
import ru.practicum.model.SortType;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.Constants.DATE_TIME_FORMAT;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventServerController {
    private final EventService service;
    private final InitiatorsCategoriesLoader initiatorsCategoriesLoader;
    private final ConfirmedRequestsLoader confirmedRequestsLoader;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping
    public List<EventPreviewResponseDto> searchEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") SortType sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<Event> events = service.searchEventsForVisitor(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
        List<EventPreviewResponseDto> resp = initiatorsCategoriesLoader.loadPreviewResponseDtos(events);
        return confirmedRequestsLoader.loadForEventDtos(resp);

    }

    @GetMapping("/{id}")
    public EventFullResponseDto getEventForVisitor(@PathVariable Long id) {
        Event event = service.getEventForVisitor(id);
        EventFullResponseDto resp = EventMapper.toFullResponseDto(event);
        resp.setCategory(CategoryMapper.toEventCategoryDto(event.getCategory()));
        confirmedRequestsLoader.loadForEventDtos(List.of(resp));

        List<Comment> comments = commentService.findAllByEventId(id);
        Map<Long, Initiator> initiators = new HashMap<>();
        for (Comment comment : comments) {
            initiators.put(comment.getCommentator().getId(), null);
        }
        initiators.put(event.getInitiator().getId(), null);
        userService.findInitiatorsByIds(initiators.keySet())
                .forEach(initiator -> initiators.put(initiator.getId(), initiator));
        for (Comment comment : comments) {
            CommentRespDto commentRespDto = CommentMapper.toDto(comment);
            commentRespDto.setCommentator(initiators.get(comment.getCommentator().getId()));
            resp.getComments().add(commentRespDto);
        }
        resp.setInitiator(initiators.get(event.getInitiator().getId()));

        return resp;
    }
}
