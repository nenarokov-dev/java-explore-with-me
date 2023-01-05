package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.client.EventStatsClient;
import ru.practicum.explorewithme.component.BeanFinder;
import ru.practicum.explorewithme.component.DateTimeAdapter;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.exceptions.ForbiddenException;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.model.category.EventCategory;
import ru.practicum.explorewithme.model.event.dto.EventDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputShortDto;
import ru.practicum.explorewithme.model.location.Location;
import ru.practicum.explorewithme.model.location.mapper.LocationMapper;
import ru.practicum.explorewithme.model.event.mapper.EventMapper;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.model.user.mapper.UserMapper;
import ru.practicum.explorewithme.pagination.Pagination;
import ru.practicum.explorewithme.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventLocationRepository eventLocationRepository;
    private final CategoriesRepository categoriesRepository;
    private final Pagination<EventOutputShortDto> paginationShort;
    private final Pagination<EventOutputDto> pagination;

    private final EventStatsClient eventStatsClient;

    public EventOutputDto add(EventDto eventDto, Long userId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        EventCategory category = BeanFinder.findEventCategoryById(eventDto.getCategory(), categoriesRepository);
        Location location = eventLocationRepository.save(LocationMapper.toLocationFromDto(eventDto.getLocation()));
        Event newEvent = EventMapper.toEventFromEventDto(eventDto, category, location, user);
        newEvent.setLocation(location);
        Event event = eventRepository.save(newEvent);
        log.info("Событие id={} успешно сохранено.", event.getId());
        return EventMapper.toEventOutputDtoFromEvent(event);
    }

    public EventOutputDto getById(Long userId, Long eventId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        Event event = eventRepository.getReferenceById(eventId);
        log.info("Событие id={} успешно получено.", eventId);
        return EventMapper.toEventOutputDtoFromEvent(event);
    }

    public EventOutputDto update(EventDto eventDto, Long userId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        BeanFinder.findEventById(eventDto.getEventId(), eventRepository);
        System.out.println(eventDto);
        Event event = eventRepository.getReferenceById(eventDto.getEventId());
        System.out.println(event);
        if (!user.equals(event.getInitiator())) {
            String message = "Только инициатор события может его изменить.";
            log.warn(message);
            throw new ForbiddenException(message);
        }
        if (event.getState() == EventState.PUBLISHED) {
            String message = "Только события, обладающие статусом 'PENDING' или 'CANCELED' могут быть изменены.";
            log.warn(message);
            throw new ForbiddenException(message);
        }
        if (event.getState() == EventState.CANCELED) {
            event.setState(EventState.PENDING);
        }
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
        return EventMapper.toEventOutputDtoFromEvent(event);
    }

    public EventOutputDto updateByAdm(EventDto eventDto,Long eventId) {
        BeanFinder.findEventById(eventId, eventRepository);
        Event event = eventRepository.getReferenceById(eventId);
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
        return EventMapper.toEventOutputDtoFromEvent(event);
    }

    public List<EventOutputDto> getAllByInitiator(Long userId, Integer from, Integer size) {
        User user = BeanFinder.findUserById(userId, userRepository);
        List<EventOutputDto> events = eventRepository.findAllByInitiatorId(userId).stream()
                .map(EventMapper::toEventOutputDtoFromEvent)
                .collect(Collectors.toList());
        log.info("Список событий,созданных пользователем id={} успешно получен.", user.getId());
        return pagination.setPagination(from, size, events);
    }

    public EventOutputDto publish(Long eventId) {
        BeanFinder.findEventById(eventId, eventRepository);
        Event event = eventRepository.getReferenceById(eventId);
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
        return EventMapper.toEventOutputDtoFromEvent(publishedEvent);
    }

    public EventOutputDto reject(Long eventId) {
        BeanFinder.findEventById(eventId, eventRepository);
        Event event = eventRepository.getReferenceById(eventId);
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
        return EventMapper.toEventOutputDtoFromEvent(canceledEvent);
    }

    public EventOutputDto cancelEvent(Long userId, Long eventId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        BeanFinder.findEventById(eventId, eventRepository);
        Event event = eventRepository.getReferenceById(eventId);
        if (!user.equals(event.getInitiator())) {
            String message = "Только инициатор события может его отменить.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        if (event.getState() == EventState.CANCELED) {
            String message = "Событие уже было отменено.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        log.info("Событие id={} отменено.", eventId);
        return EventMapper.toEventOutputDtoFromEvent(event);
    }

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
        LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeAdapter.formatter);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeAdapter.formatter);
        List<Event> events;
        System.out.println(sort);
        System.out.println(text);
        System.out.println(paid);
        System.out.println(rangeStart);
        System.out.println(rangeEnd);
        System.out.println(onlyAvailable);
        if (sort.equalsIgnoreCase("event_date")) {
            events = eventRepository.findAllByStateOrderByEventDateDesc(EventState.PUBLISHED);
        } else if (sort.equalsIgnoreCase("views")) {
            events = eventRepository.findAllByStateOrderByViewsDesc(EventState.PUBLISHED);
        } else {
            events = eventRepository.findAllByStateOrderById(EventState.PUBLISHED);
        }
        events.forEach(System.out::println);
        System.out.println(events.size());
        if (!text.isBlank()) {
            events = events.stream()
                    .filter(e -> (e.getAnnotation().toLowerCase()).contains(text.toLowerCase())||
                            (e.getDescription().toLowerCase()).contains(text.toLowerCase()))
                    .collect(Collectors.toList());
        }
        events.forEach(System.out::println);
        if (categories.length != 0) {
            List<Long> categoriesId = Stream.of(categories).map(Long::valueOf).collect(Collectors.toList());
            events = events.stream()
                    .filter(e -> categoriesId.contains(e.getCategory().getId()))
                    .collect(Collectors.toList());
        }
        events.forEach(System.out::println);
        if (paid != null) {
            events = events.stream()
                    .filter(e -> paid.equals(e.isPaid()))
                    .collect(Collectors.toList());
        }
        events.forEach(System.out::println);
        System.out.println("перед датой");
        if (!rangeStart.isBlank()) {
            if (!rangeEnd.isBlank()) {
                if (start.isAfter(end)) {
                    String message = "Неверно указан диапазон дат поиска.";
                    log.warn(message);
                    throw new BadRequestException(message);
                }
                System.out.println(start);
                System.out.println(end);
                events.forEach(e-> System.out.println(e.getEventDate()));
                events = events.stream()
                        .filter(e -> e.getEventDate().isAfter(start))
                        .filter(e -> e.getEventDate().isBefore(end))
                        .collect(Collectors.toList());
            }
        }
        events.forEach(System.out::println);
        if (onlyAvailable != null) {
            if (onlyAvailable.equals(false)) {
                events = events.stream()
                        .filter(e -> e.getConfirmedRequests() < e.getParticipantLimit())
                        .collect(Collectors.toList());
            }
        }
        events.forEach(System.out::println);
        if (onlyAvailable != null && onlyAvailable.equals(true)) {
            events = events.stream()
                    .filter(e -> e.getConfirmedRequests() < e.getParticipantLimit())
                    .collect(Collectors.toList());
        }
        events.forEach(System.out::println);
        List<EventOutputShortDto> filteredEvents = events.stream()
                .map(e -> EventMapper.toEventOutputShortDto(e, 0L, UserMapper.toUserShortDto(e.getInitiator())))
                .collect(Collectors.toList());
        EndpointHit hit = EndpointHit.builder()
                .uri(request.getRequestURI())
                .app("ewm-main-app")
                .ip(request.getRemoteAddr())
                .build();
        eventStatsClient.hit(hit);
        log.info("Отсортированный список событий успешно получен.");
        System.out.println(filteredEvents);
        return paginationShort.setPagination(from, size, filteredEvents);
    }

    public List<EventOutputDto> getAllByAdm(Integer[] users, String[] states, Integer[] categories,
                                            String rangeStart, String rangeEnd,
                                            Integer from, Integer size) {
        List<Event> events = eventRepository.findAll();
        if (users!=null) {
            List<Long> userIds = Stream.of(users).map(Long::valueOf).collect(Collectors.toList());
            events = events.stream()
                    .filter(e -> userIds.contains(e.getInitiator().getId()))
                    .collect(Collectors.toList());
        }
        if (states!=null) {
            List<EventState> eventStates = Stream.of(states).map(EventState::valueOf).collect(Collectors.toList());
            events = events.stream()
                    .filter(e -> eventStates.contains(e.getState()))
                    .collect(Collectors.toList());
        }
        if (categories!=null) {
            List<Long> categoriesId = Stream.of(categories).map(Long::valueOf).collect(Collectors.toList());
            events = events.stream()
                    .filter(e -> categoriesId.contains(e.getCategory().getId()))
                    .collect(Collectors.toList());
        }
        if (rangeStart!=null&&!rangeStart.isEmpty()) {
            LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeAdapter.formatter);
            events = events.stream()
                    .filter(e -> e.getEventDate().isAfter(start))
                    .collect(Collectors.toList());
            if (rangeEnd!=null&&!rangeEnd.isEmpty()) {
                LocalDateTime end=LocalDateTime.parse(rangeEnd, DateTimeAdapter.formatter);
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
        List<EventOutputDto> filteredEvents = events.stream()
                .map(EventMapper::toEventOutputDtoFromEvent)
                .collect(Collectors.toList());
        log.info("Отсортированный список событий успешно получен.");
        System.out.println(filteredEvents);
        return pagination.setPagination(from, size, filteredEvents);
    }


    public EventOutputDto getByIdPublic(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.getReferenceById(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            String message = "Нет доступа к информации о событии. Событие ещё не опубликовано.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        log.info("Событие id={} успешно получено.", eventId);
        EndpointHit hit = EndpointHit.builder()
                .uri(request.getRequestURI())
                .app("ewm-main-app")
                .ip(request.getRemoteAddr())
                .build();
        eventStatsClient.hit(hit);
        event.setViews(event.getViews()+1);
        Event viewedEvent = eventRepository.save(event);
        return EventMapper.toEventOutputDtoFromEvent(viewedEvent);
    }

}
