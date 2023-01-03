package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.event.category.EventCategory;

@Repository
public interface CategoriesRepository extends JpaRepository<EventCategory, Long> {

}