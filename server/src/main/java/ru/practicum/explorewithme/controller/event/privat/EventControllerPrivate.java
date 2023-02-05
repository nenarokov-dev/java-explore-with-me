package ru.practicum.explorewithme.controller.event.privat;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.event.dto.EventDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputShortDto;
import ru.practicum.explorewithme.service.EventService;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Validated
public class EventControllerPrivate {

    private final EventService eventService;

    @PostMapping("/{userId}/events")
    public EventOutputDto add(@RequestBody @Valid EventDto eventDto,
                              @PathVariable Long userId) {
        return eventService.add(eventDto, userId);
    }

    @GetMapping("/{userId}/events")
    public List<EventOutputDto> getAll(@PathVariable Long userId,
                                       @RequestParam(required = false, defaultValue = "0") Integer from,
                                       @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getAllByInitiator(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventOutputDto getById(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getById(userId, eventId);
    }

    @PatchMapping("/{userId}/events")
    public EventOutputDto update(@RequestBody @Valid EventDto eventDto, @PathVariable Long userId) {
        return eventService.update(eventDto, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventOutputDto cancel(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{userId}/subscribe/events")
    public List<EventOutputShortDto> getAllEventsByFollow(
            @PathVariable Long userId,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getActualEventsByFollow(userId, paid, onlyAvailable, sort, from, size);
    }


}
