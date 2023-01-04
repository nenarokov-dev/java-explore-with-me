package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.component.BeanFinder;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.model.event.category.EventCategory;
import ru.practicum.explorewithme.model.event.compilation.EventCompilation;
import ru.practicum.explorewithme.model.event.dto.EventDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.model.event.location.Location;
import ru.practicum.explorewithme.model.event.location.mapper.LocationMapper;
import ru.practicum.explorewithme.model.event.mapper.EventMapper;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.model.user.mapper.UserMapper;
import ru.practicum.explorewithme.pagination.Pagination;
import ru.practicum.explorewithme.repository.*;

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

    private final CompilationRepository compilationRepository;

    private final Pagination<Event> pagination;

    public EventOutputDto add(EventDto eventDto, Long userId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        EventCategory category = BeanFinder.findEventCategoryById(eventDto.getCategory(), categoriesRepository);
        Location location = eventLocationRepository.save(LocationMapper.toLocationFromDto(eventDto.getLocation()));
        Event newEvent = EventMapper.toEventFromEventDto(eventDto, category, location, user);
        newEvent.setLocation(location);
        Event event = eventRepository.save(newEvent);
        log.info("Событие id={} успешно сохранено.", event.getId());
        return EventMapper.toEventOutputDtoFromEvent(event, 0L, 0L);
    }

    public EventOutputDto getById(Long userId, Long eventId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        Event event = eventRepository.getReferenceById(eventId);
        log.info("Событие id={} успешно получено.", eventId);
        return EventMapper.toEventOutputDtoFromEvent(event, null, null);
    }

    public EventOutputDto update(EventDto eventDto, Long userId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        BeanFinder.findEventById(eventDto.getId(), eventRepository);
        Event event = eventRepository.getReferenceById(eventDto.getId());
        if (event.getState() == EventState.PUBLISHED) {
            String message = "Only pending or canceled events can be changed";
            log.warn(message);
            throw new BadRequestException(message);
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
            event.setDescription(event.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        event.setPaid(eventDto.isPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        Event eventAfterUpdate = eventRepository.save(event);
        log.info("Событие id={} успешно обновлено.", eventAfterUpdate.getId());
        return EventMapper.toEventOutputDtoFromEvent(event, null, null);
    }
}
