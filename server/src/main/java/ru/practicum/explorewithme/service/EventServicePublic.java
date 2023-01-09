package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.client.EventStatsClient;
import ru.practicum.explorewithme.component.DateTimeAdapter;
import ru.practicum.explorewithme.component.EventBuildHelper;
import ru.practicum.explorewithme.component.EventValuesCollector;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputShortDto;
import ru.practicum.explorewithme.model.event.mapper.EventMapper;
import ru.practicum.explorewithme.pagination.Pagination;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class EventServicePublic {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final EventStatsClient statsClient;
    private final Pagination<EventOutputShortDto> paginationShort;

    public List<EventOutputShortDto> getAll(String text,
                                            Integer[] categories,
                                            Boolean paid,
                                            String rangeStart,
                                            String rangeEnd,
                                            Boolean onlyAvailable,
                                            String sort,
                                            Integer from,
                                            Integer size,
                                            HttpServletRequest request) {
        List<Event> events = eventRepository.findAllByState(EventState.PUBLISHED);
        if (text != null && !text.isEmpty()) {
            events = events.stream()
                    .filter(e -> (e.getAnnotation().toLowerCase()).contains(text.toLowerCase()) ||
                            (e.getDescription().toLowerCase()).contains(text.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (categories != null && categories.length != 0) {
            List<Long> categoriesId = Stream.of(categories).map(Long::valueOf).collect(Collectors.toList());
            events = events.stream()
                    .filter(e -> categoriesId.contains(e.getCategory().getId()))
                    .collect(Collectors.toList());
        }
        if (paid != null) {
            events = events.stream()
                    .filter(e -> paid.equals(e.isPaid()))
                    .collect(Collectors.toList());
        }
        if (rangeStart != null) {
            LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeAdapter.formatter);
            if (rangeEnd != null) {
                LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeAdapter.formatter);
                if (start.isAfter(end)) {
                    String message = "Неверно указан диапазон дат поиска.";
                    log.warn(message);
                    throw new BadRequestException(message);
                }
                events = events.stream()
                        .filter(e -> e.getEventDate().isAfter(start))
                        .filter(e -> e.getEventDate().isBefore(end))
                        .collect(Collectors.toList());
            }
        } else {
            events = events.stream()
                    .filter(e -> e.getEventDate().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }
        if (onlyAvailable != null && onlyAvailable.equals(true)) {
            events = events.stream()
                    .filter(e -> EventValuesCollector.getConfirmedRequest(e.getId(), requestRepository) <
                            e.getParticipantLimit())
                    .collect(Collectors.toList());
        }
        hit(request);
        List<EventOutputShortDto> filteredEvents = EventBuildHelper.getEventOutputShortDto(events, requestRepository,
                statsClient);
        if (sort != null) {
            if (sort.equalsIgnoreCase("event_date")) {
                filteredEvents = filteredEvents.stream().sorted(Comparator.comparing(EventOutputShortDto::getEventDate))
                        .collect(Collectors.toList());
            } else if (sort.equalsIgnoreCase("views")) {
                filteredEvents = filteredEvents.stream().sorted(Comparator.comparing(EventOutputShortDto::getViews))
                        .collect(Collectors.toList());
            }
        }
        log.info("Отсортированный список событий успешно получен.");
        return paginationShort.setPagination(from, size, filteredEvents);
    }

    public EventOutputDto getById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.getReferenceById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            String message = "Нет доступа к информации о событии. Событие ещё не опубликовано.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        log.info("Событие id={} успешно получено.", eventId);
        hit(request);
        Long confirmedRequests = EventValuesCollector.getConfirmedRequest(eventId, requestRepository);
        Long views = EventValuesCollector.getEventViews(List.of(eventId), statsClient).get(eventId);
        return EventMapper.toEventOutputDtoFromEvent(event, confirmedRequests, views);
    }

    private void hit(HttpServletRequest request) {
        EndpointHit hit = EndpointHit.builder()
                .uri(request.getRequestURI())
                .app("ewm-main-app")
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeAdapter.formatter))
                .build();
        statsClient.hit(hit);
        log.info("Запрос по адресу {} успешно отправлен.", request.getRequestURI());
    }

}
