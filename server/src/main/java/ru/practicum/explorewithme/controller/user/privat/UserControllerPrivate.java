package ru.practicum.explorewithme.controller.user.privat;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.model.user.dto.UserDto;
import ru.practicum.explorewithme.service.UserService;

/**
 * TODO Sprint add-controllers.
 */

@AllArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/users/{userId}")
public class UserControllerPrivate {

    private final UserService userService;

    @PatchMapping("/subscribe/{followId}")
    public UserDto subscribe(@PathVariable Long userId, @PathVariable Long followId) {
        return userService.subscribe(userId, followId);
    }

    @PatchMapping("/unsubscribe/{followId}")
    public UserDto unsubscribe(@PathVariable Long userId, @PathVariable Long followId) {
        return userService.unsubscribe(userId, followId);
    }

}
