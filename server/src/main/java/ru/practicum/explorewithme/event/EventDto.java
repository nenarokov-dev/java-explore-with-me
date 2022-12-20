package ru.practicum.explorewithme.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class EventDto {
    String annotation;
    Long category;
    String description;
    LocalDateTime eventDate;
    Location location;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
    String title;
}
