package ru.practicum.explorewithme.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.EndpointHitDto;
import ru.practicum.explorewithme.model.ViewStats;
import ru.practicum.explorewithme.service.StatsService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@AllArgsConstructor
@Validated
@RestController
@RequestMapping
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public EndpointHitDto hit(@RequestBody @Valid EndpointHitDto hit) {
        return statsService.saveHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam(required = false) String[] uris,
                                    @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return statsService.getAllViews(start, end, uris, unique);
    }

}
