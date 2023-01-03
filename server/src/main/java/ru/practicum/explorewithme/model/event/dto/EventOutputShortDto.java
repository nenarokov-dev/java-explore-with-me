package ru.practicum.explorewithme.model.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.model.event.category.dto.CategoryDto;
import ru.practicum.explorewithme.model.event.location.Location;
import ru.practicum.explorewithme.model.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class EventOutputShortDto {

    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private String eventDate;
    private String createdOn;
    private UserShortDto initiator;
    private boolean paid;
    private Long views;
    private Long confirmedRequests;
}
