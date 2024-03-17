package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.comment.model.CommentsCount;
import ru.practicum.comment.service.CommentService;
import ru.practicum.dto.EventPreviewResponseDto;
import ru.practicum.dto.EventResponseDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CommentsLoader {
    private final CommentService service;

    public void loadForEventPreviewDtos(Collection<EventPreviewResponseDto> events) {
        Set<Long> eventIds = events.stream()
                .map(EventPreviewResponseDto::getId).collect(Collectors.toSet());
        Map<Long, Long> commentCounts = service.getCommentsCountByEventIds(eventIds).stream()
                .collect(Collectors.toMap(CommentsCount::getEventId, CommentsCount::getCount));
        for (EventPreviewResponseDto event : events) {
            event.setComments(commentCounts.get(event.getId()));
        }

    }

    public void loadForEventResponseDtos(List<EventResponseDto> events) {
        Set<Long> eventIds = events.stream()
                .map(EventResponseDto::getId).collect(Collectors.toSet());
        Map<Long, Long> commentCounts = service.getCommentsCountByEventIds(eventIds).stream()
                .collect(Collectors.toMap(CommentsCount::getEventId, CommentsCount::getCount));
        for (EventResponseDto event : events) {
            event.setComments(commentCounts.get(event.getId()));
        }

    }

    public void loadForEventPreviewDtos(Set<EventPreviewResponseDto> events) {
        Set<Long> eventIds = events.stream()
                .map(EventPreviewResponseDto::getId).collect(Collectors.toSet());
        Map<Long, Long> commentCounts = service.getCommentsCountByEventIds(eventIds).stream()
                .collect(Collectors.toMap(CommentsCount::getEventId, CommentsCount::getCount));
        for (EventPreviewResponseDto event : events) {
            event.setComments(commentCounts.get(event.getId()));
        }

    }
}
