package ru.practicum.explorewithme.controller.event.category;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.event.category.EventCategory;
import ru.practicum.explorewithme.service.EventCategoryService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/categories")
public class EventCategoryControllerPubl {

    private final EventCategoryService eventCategoryService;

    @GetMapping
    public List<EventCategory> getAll(@RequestParam Integer from, @RequestParam Integer size) {
        return eventCategoryService.getAll(from,size);
    }

    @GetMapping("/{catId}")
    public EventCategory getById(@PathVariable Long catId) {
        return eventCategoryService.getById(catId);
    }


}
