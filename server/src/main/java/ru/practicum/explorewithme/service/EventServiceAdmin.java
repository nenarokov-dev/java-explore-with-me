package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.client.EventStatsClient;
import ru.practicum.explorewithme.component.BeanFinder;
import ru.practicum.explorewithme.component.DateTimeAdapter;
import ru.practicum.explorewithme.component.EventBuildHelper;
import ru.practicum.explorewithme.component.EventValuesCollector;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.model.category.EventCategory;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.model.event.dto.EventDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.model.event.mapper.EventMapper;
import ru.practicum.explorewithme.pagination.Pagination;
import ru.practicum.explorewithme.repository.CategoriesRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class EventServiceAdmin {

    private final EventRepository eventRepository;
    private final CategoriesRepository categoriesRepository;
    private final RequestRepository requestRepository;
    private final EventStatsClient statsClient;
    private final Pagination<EventOutputDto> pagination;

    public EventOutputDto update(EventDto eventDto, Long eventId) {
        Event event = BeanFinder.findEventById(eventId, eventRepository);
        Long confirmedRequests = EventValuesCollector.getConfirmedRequest(eventId, requestRepository);
        Long views = EventValuesCollector.getEventViews(List.of(eventId), statsClient).get(eventId);
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            EventCategory eventCategory = BeanFinder.findEventCategoryById(eventDto.getCategory(), categoriesRepository);
            event.setCategory(eventCategory);
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        Event eventAfterUpdate = eventRepository.save(event);
        log.info("Событие id={} успешно обновлено.", eventAfterUpdate.getId());
        return EventMapper.toEventOutputDtoFromEvent(event, confirmedRequests, views);
    }

    public EventOutputDto publish(Long eventId) {
        Event event = BeanFinder.findEventById(eventId, eventRepository);
        Long confirmedRequests = EventValuesCollector.getConfirmedRequest(eventId, requestRepository);
        Long views = EventValuesCollector.getEventViews(List.of(eventId), statsClient).get(eventId);
        if (!event.getState().equals(EventState.PENDING)) {
            String message = "Публиковать можно события, обладающие статусом 'PENDING'.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        event.setPublishedOn(LocalDateTime.now());
        if (event.getEventDate().isBefore(event.getPublishedOn().plusHours(1))) {
            String message = "Событие уже неозможно опубликовать. Событие состоится менее чем через час.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        event.setState(EventState.PUBLISHED);
        Event publishedEvent = eventRepository.save(event);
        log.info("Событие id={} успешно опубликовано.", eventId);
        return EventMapper.toEventOutputDtoFromEvent(publishedEvent, confirmedRequests, views);
    }

    public EventOutputDto reject(Long eventId) {
        Event event = BeanFinder.findEventById(eventId, eventRepository);
        Long confirmedRequests = EventValuesCollector.getConfirmedRequest(eventId, requestRepository);
        Long views = EventValuesCollector.getEventViews(List.of(eventId), statsClient).get(eventId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            String message = "Невозможно отменить опубликованное событие.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        if (event.getState().equals(EventState.CANCELED)) {
            String message = "Событие id=" + eventId + " уже отменено.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        event.setState(EventState.CANCELED);
        Event canceledEvent = eventRepository.save(event);
        log.info("Событие id={} отменено.", eventId);
        return EventMapper.toEventOutputDtoFromEvent(canceledEvent, confirmedRequests, views);
    }

    public List<EventOutputDto> getAll(Integer[] users, String[] states, Integer[] categories,
                                       String rangeStart, String rangeEnd,
                                       Integer from, Integer size) {
        List<Event> events = eventRepository.findAll();
        if (users != null) {
            List<Long> userIds = Stream.of(users).map(Long::valueOf).collect(Collectors.toList());
            events = events.stream()
                    .filter(e -> userIds.contains(e.getInitiator().getId()))
                    .collect(Collectors.toList());
        }
        if (states != null) {
            List<EventState> eventStates = Stream.of(states).map(EventState::valueOf).collect(Collectors.toList());
            events = events.stream()
                    .filter(e -> eventStates.contains(e.getState()))
                    .collect(Collectors.toList());
        }
        if (categories != null) {
            List<Long> categoriesId = Stream.of(categories).map(Long::valueOf).collect(Collectors.toList());
            events = events.stream()
                    .filter(e -> categoriesId.contains(e.getCategory().getId()))
                    .collect(Collectors.toList());
        }
        if (rangeStart != null && !rangeStart.isEmpty()) {
            LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeAdapter.formatter);
            events = events.stream()
                    .filter(e -> e.getEventDate().isAfter(start))
                    .collect(Collectors.toList());
            if (rangeEnd != null && !rangeEnd.isEmpty()) {
                LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeAdapter.formatter);
                if (start.isAfter(end)) {
                    String message = "Неверно указан диапазон дат поиска.";
                    log.warn(message);
                    throw new BadRequestException(message);
                }
                events = events.stream()
                        .filter(e -> e.getEventDate().isBefore(end))
                        .collect(Collectors.toList());
            }
        }
        List<EventOutputDto> filteredEvents = EventBuildHelper.getEventOutputDto(events, requestRepository, statsClient);
        log.info("Отсортированный список событий успешно получен.");
        System.out.println(filteredEvents);
        return pagination.setPagination(from, size, filteredEvents);
    }
}
