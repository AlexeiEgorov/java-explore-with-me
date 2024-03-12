package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;
import ru.practicum.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    @Modifying
    @Query("update Event e SET e.confirmedRequests = ?1 where e.id = ?2")
    void updConfirmedRequests(Integer confirmedRequests, Long id);

    @Modifying
    @Query("UPDATE Event e SET e.confirmedRequests = e.confirmedRequests + 1 where e.id = ?1")
    void incrementConfirmedRequests(Long eventId);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (e.initiator.id IN :users OR :users IS null) " +
            "AND (e.state IN :states OR :states IS null) " +
            "AND (e.category.id IN :categories OR :categories IS null) " +
            "AND (e.eventDate > :start OR CAST(:start AS LocalDateTime) IS null) " +
            "AND (e.eventDate < :end OR CAST(:end AS LocalDateTime) IS null) ")
    List<Event> findAllByParam(List<Long> users, List<EventStatus> states, List<Long> categories, LocalDateTime start,
                               LocalDateTime end, PageRequest pageRequest);

    @Query(value = "SELECT e FROM Event AS e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "OR (CAST(:rangeStart AS date) IS NULL AND CAST(:rangeStart AS date) IS NULL)" +
            "OR (CAST(:rangeStart AS date) IS NULL AND e.eventDate < CAST(:rangeEnd AS date)) " +
            "OR (CAST(:rangeEnd AS date) IS NULL AND e.eventDate > CAST(:rangeStart AS date)) " +
            "GROUP BY e.id " +
            "ORDER BY e.id ASC")
    List<Event> findEventsByAdminFromParam(@Param("users") List<Long> users,
                                           @Param("states") List<EventStatus> states,
                                           @Param("categories") List<Long> categories,
                                           @Param("rangeStart") LocalDateTime rangeStart,
                                           @Param("rangeEnd") LocalDateTime rangeEnd,
                                           PageRequest pageRequest);
}
