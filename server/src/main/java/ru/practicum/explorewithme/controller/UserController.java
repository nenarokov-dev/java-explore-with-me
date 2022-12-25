package ru.practicum.explorewithme.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.dto.user.UserDto;
import ru.practicum.explorewithme.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */

@AllArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto add(@RequestBody @Valid UserDto user) {
        return userService.add(user);
    }

    @GetMapping()
    public List<UserDto> getAll(HttpServletRequest request) {
        //Map<String,String[]> collection = request.getParameterMap();
        return userService.getAll(request.getParameterMap().get("ids"),
                Integer.parseInt(request.getParameterMap().get("from")[0]),
                Integer.parseInt(request.getParameterMap().get("size")[0]));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userForUpdate, @PathVariable Long userId) {
        return userService.update(userForUpdate,userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }

}
