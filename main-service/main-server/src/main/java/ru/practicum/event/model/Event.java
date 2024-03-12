package ru.practicum.event.model;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import ru.practicum.category.model.Category;
import ru.practicum.model.Location;
import ru.practicum.model.EventStatus;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "events")
public class Event {
    @Id
    @Access(AccessType.PROPERTY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;
    @Embedded
    private Location location;
    @ColumnDefault("false")
    private Boolean paid;
    @Column(name = "participant_limit")
    @ColumnDefault("0")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    @ColumnDefault("true")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventStatus state;
    private String title;

    public Event(Long id) {
        this.id = id;
    }
}
