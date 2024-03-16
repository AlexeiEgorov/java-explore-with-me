package ru.practicum.eventrequest.model;

import lombok.*;
import ru.practicum.dto.EventRequestStatus;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_requests")
@EqualsAndHashCode
public class EventRequest {
    @Id
    @Access(AccessType.PROPERTY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    private User requester;
    @Enumerated(EnumType.STRING)
    private EventRequestStatus status;
}
