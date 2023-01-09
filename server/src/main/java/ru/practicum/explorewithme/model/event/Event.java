package ru.practicum.explorewithme.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.model.category.EventCategory;
import ru.practicum.explorewithme.model.location.Location;
import ru.practicum.explorewithme.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
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
    @OneToOne
    @JoinColumn(name = "category_id")
    private EventCategory category;
    @OneToOne
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
}
