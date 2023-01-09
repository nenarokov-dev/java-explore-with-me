package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.client.EventStatsClient;
import ru.practicum.explorewithme.component.BeanFinder;
import ru.practicum.explorewithme.component.EventBuildHelper;
import ru.practicum.explorewithme.component.EventValuesCollector;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.exceptions.ForbiddenException;
import ru.practicum.explorewithme.model.category.EventCategory;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.model.event.dto.EventDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.model.event.mapper.EventMapper;
import ru.practicum.explorewithme.model.location.Location;
import ru.practicum.explorewithme.model.location.mapper.LocationMapper;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.pagination.Pagination;
import ru.practicum.explorewithme.repository.*;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventLocationRepository eventLocationRepository;
    private final CategoriesRepository categoriesRepository;
    private final EventStatsClient statsClient;
    private final Pagination<EventOutputDto> pagination;

    public EventOutputDto add(EventDto eventDto, Long userId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        EventCategory category = BeanFinder.findEventCategoryById(eventDto.getCategory(), categoriesRepository);
        Location location = eventLocationRepository.save(LocationMapper.toLocationFromDto(eventDto.getLocation()));
        Event newEvent = EventMapper.toEventFromEventDto(eventDto, category, location, user);
        Event event = eventRepository.save(newEvent);
        log.info("Событие id={} успешно сохранено.", event.getId());
        return EventMapper.toEventOutputDtoFromEvent(event, 0L, 0L);
    }

    public EventOutputDto getById(Long userId, Long eventId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        Event event = eventRepository.getReferenceById(eventId);
        Long confirmedRequests = EventValuesCollector.getConfirmedRequest(eventId, requestRepository);
        Long views = EventValuesCollector.getEventViews(List.of(eventId), statsClient).get(eventId);
        log.info("Событие id={} успешно получено.", eventId);
        return EventMapper.toEventOutputDtoFromEvent(event, confirmedRequests, views);
    }

    public EventOutputDto update(EventDto eventDto, Long userId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        Long eventId = eventDto.getEventId();
        BeanFinder.findEventById(eventId, eventRepository);
        Event event = eventRepository.getReferenceById(eventId);
        Long confirmedRequests = EventValuesCollector.getConfirmedRequest(eventId, requestRepository);
        Long views = EventValuesCollector.getEventViews(List.of(eventId), statsClient).get(eventId);
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
        return EventMapper.toEventOutputDtoFromEvent(event, confirmedRequests, views);
    }

    public List<EventOutputDto> getAllByInitiator(Long userId, Integer from, Integer size) {
        User user = BeanFinder.findUserById(userId, userRepository);
        List<Event> events = eventRepository.findAllByInitiatorId(userId);
        List<EventOutputDto> eventOutput = EventBuildHelper.getEventOutputDto(events, requestRepository, statsClient);
        log.info("Список событий,созданных пользователем id={} успешно получен.", user.getId());
        return pagination.setPagination(from, size, eventOutput);
    }

    public EventOutputDto cancelEvent(Long userId, Long eventId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        BeanFinder.findEventById(eventId, eventRepository);
        Event event = eventRepository.getReferenceById(eventId);
        Long confirmedRequests = EventValuesCollector.getConfirmedRequest(eventId, requestRepository);
        Long views = EventValuesCollector.getEventViews(List.of(eventId), statsClient).get(eventId);
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
        return EventMapper.toEventOutputDtoFromEvent(event, confirmedRequests, views);
    }
}
