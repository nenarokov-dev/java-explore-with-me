package ru.practicum.explorewithme.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.EventCategory;
import ru.practicum.explorewithme.model.dto.user.UserDto;
import ru.practicum.explorewithme.service.EventCategoryService;
import ru.practicum.explorewithme.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
    public EventCategory add(@RequestBody @Valid EventCategory category) {
        return eventCategoryService.add(category);
    }

    @PatchMapping("/{catId}")
    public EventCategory update(@RequestBody EventCategory category) {
        return eventCategoryService.patch(category);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable Long catId) {
        eventCategoryService.delete(catId);
    }

}
