package ru.practicum.explorewithme.controller.event.admin;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.event.dto.EventDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputShortDto;
import ru.practicum.explorewithme.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/admin/events")
@Validated
public class EventControllerAdmin {

    private final EventService eventService;

    @PutMapping("/{eventId}")
    public EventOutputDto updateByAdm(@RequestBody @Valid EventDto eventDto,@PathVariable Long eventId) {
        return eventService.updateByAdm(eventDto,eventId);
    }

    @GetMapping
    public List<EventOutputDto> getAll(@RequestParam(required = false) Integer[] users,
                                            @RequestParam(required = false) String[] states,
                                            @RequestParam(required = false) Integer[] categories,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getAllByAdm(users,states, categories, rangeStart, rangeEnd, from, size);
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
