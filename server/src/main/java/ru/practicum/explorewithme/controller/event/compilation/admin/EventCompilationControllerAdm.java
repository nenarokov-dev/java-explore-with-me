package ru.practicum.explorewithme.controller.event.compilation.admin;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.event.compilation.EventCompilation;
import ru.practicum.explorewithme.model.event.compilation.dto.EventCompilationDto;
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
    public EventCompilation add(@RequestBody @Valid EventCompilationDto compilation) {
        return eventCompilationService.add(compilation);
    }

    @DeleteMapping("/{compId}")
    public void deleteById(@PathVariable Long compId) {
        eventCompilationService.deleteById(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void removeEventFromCompilation(@PathVariable Long compId,@PathVariable Long eventId) {
        eventCompilationService.removeEventFromCompilation(compId,eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public EventCompilation removeCompilationFromMainPage(@PathVariable Long compId) {
        return eventCompilationService.removeOrSetCompilationFromMainPage(compId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public EventCompilation addEventToCompilation(@PathVariable Long compId,@PathVariable Long eventId) {
        return eventCompilationService.addOtherEventToCompilation(compId,eventId);
    }

    @PatchMapping("/{compId}/pin")
    public EventCompilation setCompilationToMainPage(@PathVariable Long compId) {
        return eventCompilationService.removeOrSetCompilationFromMainPage(compId);
    }

}
