package ru.practicum.explorewithme.component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.category.EventCategory;
import ru.practicum.explorewithme.model.compilation.EventCompilation;
import ru.practicum.explorewithme.model.location.Location;
import ru.practicum.explorewithme.model.request.Request;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.repository.*;

@Component
@AllArgsConstructor
@Slf4j
public class BeanFinder {

    public static User findUserById(Long userId, UserRepository userRepository) {
        if (userRepository.findById(userId).isEmpty()) {
            String message = String.format("Не удалось найти пользователя с id=%d.", userId);
            log.warn(message);
            throw new NotFoundException(message);
        } else
            return userRepository.findById(userId).get();
    }

    public static EventCategory findEventCategoryById(Long categoryId, CategoriesRepository categoriesRepository) {
        if (categoriesRepository.findById(categoryId).isEmpty()) {
            String message = String.format("Не удалось найти категорию событий с id=%d.", categoryId);
            log.warn(message);
            throw new NotFoundException(message);
        } else
            return categoriesRepository.findById(categoryId).get();
    }

    public static Location findEventLocationById(Long locationId, EventLocationRepository eventLocationRepository) {
        if (eventLocationRepository.findById(locationId).isEmpty()) {
            String message = String.format("Не удалось найти локацию события с id=%d.", locationId);
            log.warn(message);
            throw new NotFoundException(message);
        } else
            return eventLocationRepository.findById(locationId).get();
    }

    public static Event findEventById(Long eventId, EventRepository eventRepository) {
        if (eventRepository.findById(eventId).isEmpty()) {
            String message = String.format("Не удалось найти событиe с id=%d.", eventId);
            log.warn(message);
            throw new NotFoundException(message);
        } else
            return eventRepository.findById(eventId).get();
    }

    public static Request findRequestById(Long requestId, RequestRepository requestRepository) {
        if (requestRepository.findById(requestId).isEmpty()) {
            String message = String.format("Не удалось найти запрос на участие в событии с id=%d.", requestId);
            log.warn(message);
            throw new NotFoundException(message);
        } else
            return requestRepository.findById(requestId).get();
    }

    public static EventCompilation findEventsCompilationById(Long compId, CompilationRepository compilationRepository) {
        if (compilationRepository.findById(compId).isEmpty()) {
            String message = String.format("Не удалось найти подборку событий с id=%d.", compId);
            log.warn(message);
            throw new NotFoundException(message);
        } else {
            return compilationRepository.findById(compId).get();
        }
    }
}
