package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.EventForComment;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Set<Event> findAllByIdIn(Set<Long> eventsIds);

    @Query("select new ru.practicum.dto.EventForComment(e.id, e.title) from Event e where e.id in ?1")
    List<EventForComment> getEventForCommentByIds(Set<Long> eventsIds);
}
