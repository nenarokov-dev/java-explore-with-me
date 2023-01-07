package ru.practicum.explorewithme.model.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.model.event.dto.EventOutputShortDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EventCompilationOutputDto {

    private Long id;
    private List<EventOutputShortDto> events;
    private Boolean pinned;
    private String title;
}
