package ru.practicum.explorewithme.controller.category;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.category.EventCategory;
import ru.practicum.explorewithme.service.EventCategoryService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/categories")
public class EventCategoryControllerPubl {

    private final EventCategoryService eventCategoryService;

    @GetMapping
    public List<EventCategory> getAll(@RequestParam(required = false, defaultValue = "0") Integer from,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventCategoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public EventCategory getById(@PathVariable Long catId) {
        return eventCategoryService.getById(catId);
    }


}
