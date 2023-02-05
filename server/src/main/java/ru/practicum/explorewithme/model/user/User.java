package ru.practicum.explorewithme.model.user;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "subscriptions",
            joinColumns = {@JoinColumn(name = "subscriber_id")},
            inverseJoinColumns = {@JoinColumn(name = "events_initiator_id")}
    )
    @Builder.Default
    @ToString.Exclude
    private Set<User> subscribeList = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId().equals(user.getId()) && getName().equals(user.getName())
                && getEmail().equals(user.getEmail()) && getSubscribeList().equals(user.getSubscribeList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getSubscribeList());
    }
}
