package ru.practicum.explorewithme.model.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.model.event.dto.EventOutputShortDto;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class EventCompilationOutputDto {

    private Long id;
    private Set<EventOutputShortDto> events;
    private Boolean pinned;
    private String title;
}
