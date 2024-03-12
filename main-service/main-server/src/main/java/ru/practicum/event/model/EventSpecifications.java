package ru.practicum.event.model;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public class EventSpecifications {

    public static Specification<Event> hasUsers(List<Integer> userIds) {
        return (root, query, criteriaBuilder) -> root.get("initiator").get("id").in(userIds);
    }

    public static Specification<Event> hasStates(List<EventStatus> states) {
        return (root, query, criteriaBuilder) -> root.get("state").in(states);
    }

    public static Specification<Event> hasCategories(List<Integer> categoryIds) {
        return (root, query, criteriaBuilder) -> root.get("category").get("id").in(categoryIds);
    }

    public static Specification<Event> hasEventDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return Specification.where(hasEventDateAfter(startDateTime))
                .and(hasEventDateBefore(endDateTime));
    }

    public static Specification<Event> hasEventDateAfter(LocalDateTime startDateTime) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startDateTime);
    }

    public static Specification<Event> hasEventDateBefore(LocalDateTime endDateTime) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), endDateTime);
    }

    public static Specification<Event> containsText(String text) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")),
                            "%" + text.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                            "%" + text.toLowerCase() + "%")
            );
    }

    public static Specification<Event> isPaid(Boolean paid) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("paid"), paid);
    }

    public static Specification<Event> isAvailable() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"));
    }
}