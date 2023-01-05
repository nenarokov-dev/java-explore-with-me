package ru.practicum.explorewithme.controller.compilation.admin;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.compilation.EventCompilation;
import ru.practicum.explorewithme.model.compilation.dto.EventCompilationDto;
import ru.practicum.explorewithme.model.compilation.dto.EventCompilationOutputDto;
import ru.practicum.explorewithme.service.EventCompilationService;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */

@AllArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/admin/compilations")
public class EventCompilationControllerAdm {

    private final EventCompilationService eventCompilationService;

    @PostMapping
    public EventCompilationOutputDto add(@RequestBody @Valid EventCompilationDto compilation) {
        return eventCompilationService.add(compilation);
    }

    @DeleteMapping("/{compId}")
    public void deleteById(@PathVariable Long compId) {
        eventCompilationService.deleteById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public EventCompilationOutputDto removeEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        return eventCompilationService.removeEventFromCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public EventCompilationOutputDto removeCompilationFromMainPage(@PathVariable Long compId) {
        return eventCompilationService.removeOrSetCompilationFromMainPage(compId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public EventCompilationOutputDto addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        return eventCompilationService.addOtherEventToCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/pin")
    public EventCompilationOutputDto setCompilationToMainPage(@PathVariable Long compId) {
        return eventCompilationService.removeOrSetCompilationFromMainPage(compId);
    }

}
