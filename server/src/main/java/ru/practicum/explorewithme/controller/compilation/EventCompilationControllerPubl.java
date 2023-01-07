package ru.practicum.explorewithme.controller.compilation;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.compilation.dto.EventCompilationOutputDto;
import ru.practicum.explorewithme.service.EventCompilationService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/compilations")
public class EventCompilationControllerPubl {

    private final EventCompilationService eventCompilationService;

    @GetMapping
    public List<EventCompilationOutputDto> getAll(@RequestParam(required = false) Integer from,
                                                  @RequestParam(required = false) Integer size,
                                                  @RequestParam(required = false) Boolean pinned) {
        return eventCompilationService.getAll(from, size, pinned);
    }

    @GetMapping("/{catId}")
    public EventCompilationOutputDto getById(@PathVariable Long catId) {
        return eventCompilationService.getById(catId);
    }


}
