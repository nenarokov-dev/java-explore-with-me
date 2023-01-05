package ru.practicum.explorewithme.model.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.compilation.EventCompilation;
import ru.practicum.explorewithme.model.compilation.dto.EventCompilationDto;
import ru.practicum.explorewithme.model.compilation.dto.EventCompilationOutputDto;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.dto.EventOutputShortDto;
import ru.practicum.explorewithme.model.event.mapper.EventMapper;
import ru.practicum.explorewithme.model.user.mapper.UserMapper;

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

    public static EventCompilation fromEventCompilationDto(EventCompilationDto compilationDto, Set<Event> events) {
        return EventCompilation.builder()
                .id(compilationDto.getId())
                .events(events)
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .build();
    }

    public static EventCompilationOutputDto toEventCompilationOutputDto(EventCompilation compilation) {
        Set<EventOutputShortDto> events = compilation.getEvents().stream()
                .map(e -> EventMapper
                        .toEventOutputShortDto(e, null, UserMapper.toUserShortDto(e.getInitiator())))
                .collect(Collectors.toSet());
        return EventCompilationOutputDto.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

}