package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {


    List<Event> findEventsByIdIsIn(Set<Long> id);

    List<Event> findAllByInitiatorId(Long id);

    List<Event> findAllByState(EventState state);

    List<Event> findAllByInitiator_IdInAndStateAndEventDateAfter(List<Long> ids, EventState state, LocalDateTime time);


}