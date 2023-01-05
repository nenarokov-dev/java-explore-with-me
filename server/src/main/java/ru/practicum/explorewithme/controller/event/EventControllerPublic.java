package ru.practicum.explorewithme.controller.event;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.event.dto.EventDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputShortDto;
import ru.practicum.explorewithme.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/events")
@Validated
public class EventControllerPublic {
    private final EventService eventService;

    @GetMapping
    public List<EventOutputShortDto> getAll(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) Integer[] categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        return eventService.getAll(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,request);
    }

    @GetMapping("/{eventId}")
    public EventOutputDto getById(@PathVariable Long eventId, HttpServletRequest request) {
        return eventService.getByIdPublic(eventId, request);
    }

}
