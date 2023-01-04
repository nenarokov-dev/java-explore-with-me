package ru.practicum.explorewithme.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;
import ru.practicum.explorewithme.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final String pattern = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    @PostMapping("/hit")
    public String hit(@RequestBody @Valid EndpointHit hit) {
        return statsService.saveHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam String[] uris,
                                    @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.getAllViews(LocalDateTime.parse(start,formatter), LocalDateTime.parse(end,formatter), uris, unique);
    }

}
