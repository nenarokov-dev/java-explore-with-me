package ru.practicum.explorewithme.model.event.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.component.DateTimeAdapter;
import ru.practicum.explorewithme.model.category.EventCategory;
import ru.practicum.explorewithme.model.category.dto.CategoryDto;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.model.event.dto.EventDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputShortDto;
import ru.practicum.explorewithme.model.location.Location;
import ru.practicum.explorewithme.model.location.mapper.LocationMapper;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.model.user.mapper.UserMapper;


@Component
public class EventMapper {

    public static Event toEventFromEventDto(EventDto eventDto, EventCategory category, Location location, User user) {
        return Event.builder()
                .title(eventDto.getTitle())
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .category(category)
                .eventDate(eventDto.getEventDate())
                .location(location)
                .paid(eventDto.getPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration())
                .state(EventState.PENDING)
                .initiator(user)
                .build();
    }

    public static EventOutputDto toEventOutputDtoFromEvent(Event event, Long confirmedRequests, Long views) {
        String publishedOn = null;
        if (event.getPublishedOn() != null) {
            publishedOn = event.getPublishedOn().format(DateTimeAdapter.formatter);
        }
        return EventOutputDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .eventDate(event.getEventDate().format(DateTimeAdapter.formatter))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(publishedOn)
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreated().format(DateTimeAdapter.formatter))
                .views(views)
                .build();
    }

    public static EventOutputShortDto toEventOutputShortDto(Event event, Long confirmedRequests, Long views) {
        return EventOutputShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .eventDate(event.getEventDate().format(DateTimeAdapter.formatter))
                .paid(event.isPaid())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .views(views)
                .confirmedRequests(confirmedRequests)
                .build();
    }

}