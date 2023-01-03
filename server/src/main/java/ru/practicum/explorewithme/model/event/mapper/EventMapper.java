package ru.practicum.explorewithme.model.event.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.category.EventCategory;
import ru.practicum.explorewithme.model.event.category.dto.CategoryDto;
import ru.practicum.explorewithme.model.event.dto.EventDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputShortDto;
import ru.practicum.explorewithme.model.event.location.Location;
import ru.practicum.explorewithme.model.event.location.mapper.LocationMapper;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.model.user.dto.UserShortDto;
import ru.practicum.explorewithme.model.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
                .paid(eventDto.isPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration())
                .initiator(user)
                .build();
    }

    public static EventOutputDto toEventOutputDtoFromEvent(Event event,Long views, Long confirmedRequests) {
        return EventOutputDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .category(new CategoryDto(event.getCategory().getId(),event.getCategory().getName()))
                .eventDate(convertDateTimeToString(event.getEventDate()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(convertDateTimeToString(event.getPublishedOn()))
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .views(views)
                .confirmedRequests(confirmedRequests)
                .createdOn(convertDateTimeToString(event.getCreated()))
                .build();
    }

    public static EventOutputShortDto toEventOutputShortDtoFromEvent(Event event, Long views,
                                                                     UserShortDto userShortDto,
                                                                     Long confirmedRequests) {
        return EventOutputShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(),event.getCategory().getName()))
                .eventDate(convertDateTimeToString(event.getEventDate()))
                .paid(event.isPaid())
                .initiator(userShortDto)
                .views(views)
                .confirmedRequests(confirmedRequests)
                .createdOn(convertDateTimeToString(event.getCreated()))
                .build();
    }

    private static String convertDateTimeToString(LocalDateTime dateTime){
        if (dateTime!=null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return dateTime.format(formatter);
        } else return null;
    }

}