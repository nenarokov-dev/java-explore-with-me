package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.model.dto.user.UserDto;
import ru.practicum.explorewithme.model.mapper.UserMapper;
import ru.practicum.explorewithme.pagination.Pagination;
import ru.practicum.explorewithme.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private UserRepository userStorage;

    private final Pagination<UserDto> pagination;

    public UserDto add(UserDto userDto) {
        User savedUser = userStorage.save(UserMapper.toUser(userDto));
        log.info("Пользователь id={} успешно добавлен.", savedUser.getId());
        return UserMapper.toUserDto(savedUser);
    }

    public UserDto get(Long userId) {
        if (userStorage.findById(userId).isPresent()) {
            User user = userStorage.findById(userId).get();
            log.info("Пользователь id={} успешно получен.", userId);
            return UserMapper.toUserDto(user);
        } else {
            String message = "Пользователь с id=" + userId + " не найден.";
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    public List<UserDto> getAll(String[] ids,Integer from, Integer size) {
        Long[] parsedIds = Arrays.stream(ids).map(Long::parseLong).toArray(Long[]::new);
        List<UserDto> users = userStorage.findAllWhereIdIn(parsedIds)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("Список пользователей успешно получен.");
        return pagination.setPagination(from,size,users);
    }

    public UserDto update(UserDto userForUpdate, Long userId) {
        log.info(userForUpdate.toString());
        userForUpdate.setId(userId);
        if (userStorage.findById(userId).isPresent()) {
            User user = userStorage.getReferenceById(userId);
            if (userForUpdate.getEmail() != null) {
                user.setEmail(userForUpdate.getEmail());
            }
            if (userForUpdate.getName() != null) {
                user.setName(userForUpdate.getName());
            }
            log.info("Пользователь id={} успешно обновлен.", userForUpdate.getId());
            User savedUser = userStorage.save(user);
            return UserMapper.toUserDto(savedUser);
        } else {
            String message = "Пользователь с id=" + userId + " не найден.";
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    public void delete(Long userId) {
        userStorage.deleteById(userId);
        log.info("Пользователь id={} был успешно удалён.", userId);
    }
}
