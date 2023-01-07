package ru.practicum.explorewithme.controller.category.admin;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.category.EventCategory;
import ru.practicum.explorewithme.model.category.dto.CategoryDto;
import ru.practicum.explorewithme.service.EventCategoryService;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */

@AllArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/admin/categories")
public class EventCategoryControllerAdm {

    private final EventCategoryService eventCategoryService;

    @PostMapping
    public EventCategory add(@RequestBody @Valid CategoryDto category) {
        return eventCategoryService.add(category);
    }

    @PatchMapping()
    public CategoryDto update(@RequestBody CategoryDto category) {
        return eventCategoryService.patch(category);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable Long catId) {
        eventCategoryService.delete(catId);
    }

}
