package ru.practicum.explorewithme.model.compilation;

import lombok.*;
import ru.practicum.explorewithme.model.event.Event;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations", schema = "public")
public class EventCompilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "event_compilations",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    @ToString.Exclude
    private List<Event> events;
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
    @Column(name = "title", nullable = false)
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventCompilation)) return false;
        EventCompilation that = (EventCompilation) o;
        return getId().equals(that.getId()) && getEvents().equals(that.getEvents())
                && getPinned().equals(that.getPinned()) && getTitle().equals(that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEvents(), getPinned(), getTitle());
    }
}
