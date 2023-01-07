package ru.practicum.explorewithme.controller.event.admin;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.event.dto.EventDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.service.EventServiceAdmin;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/admin/events")
@Validated
public class EventControllerAdmin {

    private final EventServiceAdmin eventService;

    @PutMapping("/{eventId}")
    public EventOutputDto update(@RequestBody @Valid EventDto eventDto, @PathVariable Long eventId) {
        return eventService.update(eventDto, eventId);
    }

    @GetMapping
    public List<EventOutputDto> getAll(@RequestParam(required = false) Integer[] users,
                                       @RequestParam(required = false) String[] states,
                                       @RequestParam(required = false) Integer[] categories,
                                       @RequestParam(required = false) String rangeStart,
                                       @RequestParam(required = false) String rangeEnd,
                                       @RequestParam(defaultValue = "0", required = false) Integer from,
                                       @RequestParam(defaultValue = "10", required = false) Integer size) {
        return eventService.getAll(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}/publish")
    public EventOutputDto publish(@PathVariable Long eventId) {
        return eventService.publish(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventOutputDto reject(@PathVariable Long eventId) {
        return eventService.reject(eventId);
    }


}
