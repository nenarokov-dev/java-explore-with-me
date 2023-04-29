package ru.practicum.explorewithme.model.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.request.Request;
import ru.practicum.explorewithme.model.request.dto.RequestDto;
import ru.practicum.explorewithme.model.user.User;


@Component
public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }

    public static Request toRequestFromDto(RequestDto requestDto, Event event, User user) {
        return Request.builder()
                .event(event)
                .requester(user)
                .created(requestDto.getCreated())
                .status(requestDto.getStatus())
                .build();
    }

}