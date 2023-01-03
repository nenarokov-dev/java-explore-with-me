package ru.practicum.explorewithme.controller.event.privatly;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.event.compilation.EventCompilation;
import ru.practicum.explorewithme.model.event.compilation.dto.EventCompilationDto;
import ru.practicum.explorewithme.model.event.dto.EventDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.service.EventCompilationService;
import ru.practicum.explorewithme.service.EventService;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Validated
public class EventCompilationControllerPrivate {

    private final EventService eventService;

    @PostMapping("{userId}/events")
    public EventOutputDto add(@RequestBody @Valid EventDto eventDto,
                              @PathVariable Long userId) {
        return eventService.add(eventDto,userId);
    }

    @GetMapping("{userId}/events/{eventId}")
    public EventOutputDto getById(@PathVariable Long userId,@PathVariable Long eventId) {
        return eventService.getById(userId,eventId);
    }

    @PatchMapping("{userId}/events")
    public EventOutputDto update(@RequestBody @Valid EventDto eventDto,@PathVariable Long userId) {
        return eventService.update(eventDto,userId);
    }


}
