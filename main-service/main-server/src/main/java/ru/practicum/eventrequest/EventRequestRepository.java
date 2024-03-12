package ru.practicum.eventrequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.EventRequestStatus;
import ru.practicum.eventrequest.model.EventRequest;

import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    List<EventRequest> findAllByRequesterId(Long requesterId);

    @Modifying
    @Query("update EventRequest er SET status = 3 where id = ?1")
    void cancel(Long reqId);

    List<EventRequest> findAllByEventId(Long eventId);

    @Modifying
    @Query("update EventRequest er SET status = ?2 where id in (?1)")
    void updateStatusOfEventRequestsByIds(List<Long> requestsIds, EventRequestStatus eventStatus);

    EventRequest findByEventIdAndRequesterId(Long eventId, Long requesterId);
}
