package ru.practicum.explorewithme.model.event.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.compilation.EventCompilation;
import ru.practicum.explorewithme.model.event.compilation.dto.EventCompilationDto;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class CompilationMapper {

    public static EventCompilationDto toEventCompilationDto(EventCompilation compilation) {
        List<Long> events = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
        return EventCompilationDto.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static EventCompilation fromEventCompilationDto(EventCompilationDto compilationDto,List<Event> events) {
        return EventCompilation.builder()
                .id(compilationDto.getId())
                .events(events)
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .build();
    }

}