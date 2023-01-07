package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.component.BeanFinder;
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
import ru.practicum.explorewithme.repository.CategoriesRepository;
import ru.practicum.explorewithme.repository.EventLocationRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventLocationRepository eventLocationRepository;
    private final CategoriesRepository categoriesRepository;
    private final Pagination<EventOutputDto> pagination;

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

    public List<EventOutputDto> getAllByInitiator(Long userId, Integer from, Integer size) {
        User user = BeanFinder.findUserById(userId, userRepository);
        List<EventOutputDto> events = eventRepository.findAllByInitiatorId(userId).stream()
                .map(EventMapper::toEventOutputDtoFromEvent)
                .collect(Collectors.toList());
        log.info("Список событий,созданных пользователем id={} успешно получен.", user.getId());
        return pagination.setPagination(from, size, events);
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

}
