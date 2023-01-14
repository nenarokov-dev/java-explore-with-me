package ru.practicum.explorewithme.model.request;

import lombok.*;
import ru.practicum.explorewithme.model.event.Event;
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
@Table(name = "requests", schema = "public")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @OneToOne
    @JoinColumn(name = "requestor_id")
    private User requester;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RequestStatus status = RequestStatus.PENDING;
    @Column(name = "created", nullable = false)
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return getId().equals(request.getId()) && getEvent().equals(request.getEvent())
                && getRequester().equals(request.getRequester()) && getStatus() == request.getStatus()
                && getCreated().equals(request.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEvent(), getRequester(), getStatus(), getCreated());
    }
}
