package ru.practicum.explorewithme.model.event;

import lombok.*;
import ru.practicum.explorewithme.model.category.EventCategory;
import ru.practicum.explorewithme.model.location.Location;
import ru.practicum.explorewithme.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events", schema = "public")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "annotation", nullable = false)
    private String annotation;
    @Column(name = "description", nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private EventCategory category;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @OneToOne
    @JoinColumn(name = "id")
    private Location location;
    @Column(name = "is_paid", nullable = false)
    private boolean paid;
    @Column(name = "participant_limit", nullable = false)
    private int participantLimit;
    @Column(name = "is_request_moderated", nullable = false)
    private Boolean requestModeration;
    @Column(name = "publication_date", nullable = false)
    private LocalDateTime publishedOn;
    @Column(name = "creation_date", nullable = false)
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();
    @Enumerated(value = EnumType.STRING)
    @Column(name = "state", nullable = false)
    private EventState state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return isPaid() == event.isPaid() && getParticipantLimit() == event.getParticipantLimit()
                && getId().equals(event.getId()) && getTitle().equals(event.getTitle())
                && getAnnotation().equals(event.getAnnotation()) && getDescription().equals(event.getDescription())
                && getCategory().equals(event.getCategory()) && getInitiator().equals(event.getInitiator())
                && getEventDate().equals(event.getEventDate()) && getLocation().equals(event.getLocation())
                && getRequestModeration().equals(event.getRequestModeration())
                && getPublishedOn().equals(event.getPublishedOn()) && getCreated().equals(event.getCreated())
                && getState() == event.getState();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getAnnotation(), getDescription(), getCategory(), getInitiator(),
                getEventDate(), getLocation(), isPaid(), getParticipantLimit(), getRequestModeration(),
                getPublishedOn(), getCreated(), getState());
    }
}
