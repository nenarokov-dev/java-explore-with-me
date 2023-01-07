package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;

import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {


    Set<Event> findEventsByIdIsIn(Set<Long> id);

    List<Event> findAllByInitiatorId(Long id);

    List<Event> findAllByStateOrderByEventDateDesc(EventState state);

    List<Event> findAllByStateOrderByViewsDesc(EventState state);

    List<Event> findAllByStateOrderById(EventState state);

    List<Event> findEventsByInitiator_IdIn(List<Long> users);

    List<Event> findEventsByInitiator_IdInAndStateInAndCategory_IdIn(List<Long> users,
                                                                     List<EventState> states,
                                                                     List<Long> categories);

}