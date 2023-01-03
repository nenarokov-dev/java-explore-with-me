package ru.practicum.explorewithme.controller.event.compilation;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.event.compilation.EventCompilation;
import ru.practicum.explorewithme.service.EventCompilationService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/compilations")
public class EventCompilationControllerPubl {

    private final EventCompilationService eventCompilationService;

    @GetMapping
    public List<EventCompilation> getAll(@RequestParam(required = false) Integer from,
                                         @RequestParam(required = false) Integer size,
                                         @RequestParam(required = false) Boolean pinned) {
        return eventCompilationService.getAll(from,size,pinned);
    }

    @GetMapping("/{catId}")
    public EventCompilation getById(@PathVariable Long catId) {
        return eventCompilationService.getById(catId);
    }


}
