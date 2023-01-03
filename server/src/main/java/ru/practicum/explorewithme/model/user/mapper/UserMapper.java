package ru.practicum.explorewithme.model.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.model.user.dto.UserDto;
import ru.practicum.explorewithme.model.user.dto.UserShortDto;


@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static UserShortDto toUserShortDto(UserDto user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User toUser(UserDto user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}