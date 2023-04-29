package ru.practicum.explorewithme.controller.user;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.user.dto.UserDto;
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
@RequestMapping(path = "/admin/users")
public class UserControllerAdm {

    private final UserService userService;

    @PostMapping
    public UserDto add(@RequestBody @Valid UserDto user) {
        return userService.add(user);
    }

    @GetMapping()
    public List<UserDto> getAll(@RequestParam(required = false) Integer from,
                                @RequestParam(required = false) Integer size,
                                HttpServletRequest request) {
        return userService.getAll(request.getParameterMap().get("ids"), from, size);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userForUpdate, @PathVariable Long userId) {
        return userService.update(userForUpdate, userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }

}
