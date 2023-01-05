package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.component.BeanFinder;
import ru.practicum.explorewithme.model.compilation.dto.EventCompilationOutputDto;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.compilation.EventCompilation;
import ru.practicum.explorewithme.model.compilation.dto.EventCompilationDto;
import ru.practicum.explorewithme.model.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.pagination.Pagination;
import ru.practicum.explorewithme.repository.CompilationRepository;
import ru.practicum.explorewithme.repository.EventRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EventCompilationService {
    private final EventRepository eventRepository;

    private final CompilationRepository compilationRepository;

    private final Pagination<EventCompilationOutputDto> pagination;

    public EventCompilationOutputDto add(EventCompilationDto eventCompilationDto) {
        Set<Event> events = eventRepository.findEventsByIdIsIn(eventCompilationDto.getEvents());
        EventCompilation compilation =
                compilationRepository.save(CompilationMapper.fromEventCompilationDto(eventCompilationDto, events));
        log.info("Подборка событий id={} успешно добавлена.", compilation.getId());
        return CompilationMapper.toEventCompilationOutputDto(compilation);
    }

    public EventCompilationOutputDto getById(Long compId) {
        EventCompilation compilation = BeanFinder.findEventsCompilationById(compId, compilationRepository);
        log.info("Подборка событий id={} успешно получена.", compId);
        return CompilationMapper.toEventCompilationOutputDto(compilation);
    }

    public List<EventCompilationOutputDto> getAll(Integer from, Integer size, Boolean pinned) {
        List<EventCompilationOutputDto> compilations;
        if (pinned != null) {
            if (pinned) {
                compilations = compilationRepository.findAllByPinnedIs(true).stream()
                        .map(CompilationMapper::toEventCompilationOutputDto)
                        .collect(Collectors.toList());
            } else {
                compilations = compilationRepository.findAllByPinnedIs(false).stream()
                        .map(CompilationMapper::toEventCompilationOutputDto)
                        .collect(Collectors.toList());
            }
        } else {
            compilations = compilationRepository.findAll().stream()
                    .map(CompilationMapper::toEventCompilationOutputDto)
                    .collect(Collectors.toList());
        }
        log.info("Подборки событий успешно получены.");
        return pagination.setPagination(from, size, compilations);
    }

    public void deleteById(Long compId) {
        compilationRepository.deleteById(compId);
        log.info("Подборка событий id={} успешно удолена.", compId);
    }

    public EventCompilationOutputDto addOtherEventToCompilation(Long compId, Long eventId) {
        BeanFinder.findEventsCompilationById(compId, compilationRepository);
        Event event = BeanFinder.findEventById(eventId, eventRepository);
        EventCompilation compilation = compilationRepository.getReferenceById(compId);
        compilation.getEvents().add(event);
        EventCompilation updatedCompilation = compilationRepository.save(compilation);
        log.info("Новое событие id={} было успешно добавлено в подборку событий id={}.", eventId, compId);
        return CompilationMapper.toEventCompilationOutputDto(updatedCompilation);
    }

    public EventCompilationOutputDto removeEventFromCompilation(Long compId, Long eventId) {
        EventCompilation compilation = compilationRepository.getReferenceById(compId);
        Event event = BeanFinder.findEventById(eventId, eventRepository);
        Set<Event> events = compilation.getEvents();
        events.remove(event);
        compilation.setEvents(events);
        EventCompilation updatedCompilation = compilationRepository.save(compilation);
        log.info("Событие id={} было успешно удалено из подборки событий id={}.", eventId, compId);
        return CompilationMapper.toEventCompilationOutputDto(updatedCompilation);
    }

    public EventCompilationOutputDto removeOrSetCompilationFromMainPage(Long compId) {
        EventCompilation compilation = compilationRepository.getReferenceById(compId);
        Boolean pinned = compilation.getPinned();
        compilation.setPinned(!pinned);
        EventCompilation updatedCompilation = compilationRepository.save(compilation);
        if (pinned) {
            log.info("Подборка событий id={} откреплена с главной страницы.", compId);
        } else {
            log.info("Подборка событий id={} закреплена на главной странице.", compId);
        }
        return CompilationMapper.toEventCompilationOutputDto(updatedCompilation);
    }


}
