package ru.practicum.explorewithme.model.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.client.EventStatsClient;
import ru.practicum.explorewithme.component.EventBuildHelper;
import ru.practicum.explorewithme.model.compilation.EventCompilation;
import ru.practicum.explorewithme.model.compilation.dto.EventCompilationDto;
import ru.practicum.explorewithme.model.compilation.dto.EventCompilationOutputDto;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.repository.RequestRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {

    public static EventCompilationDto toEventCompilationDto(EventCompilation compilation) {
        Set<Long> events = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toSet());
        return EventCompilationDto.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static EventCompilation fromEventCompilationDto(EventCompilationDto compilationDto, List<Event> events) {
        return EventCompilation.builder()
                .id(compilationDto.getId())
                .events(events)
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .build();
    }

    public static EventCompilationOutputDto toEventCompilationOutputDto(EventCompilation compilation,
                                                                        RequestRepository requestRepository,
                                                                        EventStatsClient statsClient) {
        return EventCompilationOutputDto.builder()
                .id(compilation.getId())
                .events(EventBuildHelper.getEventOutputShortDto(compilation.getEvents(), requestRepository, statsClient))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

}