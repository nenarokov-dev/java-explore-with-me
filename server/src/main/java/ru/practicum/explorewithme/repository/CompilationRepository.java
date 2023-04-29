package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.compilation.EventCompilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<EventCompilation, Long> {

    List<EventCompilation> findAllByPinnedIs(Boolean pinned);
}