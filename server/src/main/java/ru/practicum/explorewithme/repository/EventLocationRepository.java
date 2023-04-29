package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.location.Location;

@Repository
public interface EventLocationRepository extends JpaRepository<Location, Long> {

}