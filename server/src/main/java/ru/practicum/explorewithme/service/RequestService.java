package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.component.BeanFinder;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.exceptions.DuplicateException;
import ru.practicum.explorewithme.exceptions.ForbiddenException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.model.request.Request;
import ru.practicum.explorewithme.model.request.RequestStatus;
import ru.practicum.explorewithme.model.request.dto.RequestDto;
import ru.practicum.explorewithme.model.request.mapper.RequestMapper;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.model.user.dto.UserDto;
import ru.practicum.explorewithme.model.user.mapper.UserMapper;
import ru.practicum.explorewithme.pagination.Pagination;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.RequestRepository;
import ru.practicum.explorewithme.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class RequestService {

    private UserRepository userRepository;

    private EventRepository eventRepository;

    private RequestRepository requestRepository;

    private final Pagination<UserDto> pagination;

    public RequestDto add(Long userId,Long eventId) {
        if (requestRepository.findByRequesterIdAndEventId(userId,eventId)!=null){
            String message = "Пользователь id=" + userId + " уже отправил запрос на участие в событии id="+eventId+".";
            log.warn(message);
            throw new DuplicateException(message);
        }
        Event event = BeanFinder.findEventById(eventId,eventRepository);
        if (event.getInitiator().getId().equals(userId)){
            String message = "Инициатор не может создавать запросы на участие в событии.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        if (!event.getState().equals(EventState.PUBLISHED)){
            String message = "Нельзя принимать участие в неопубликованном событии.";
            log.warn(message);
            throw new ForbiddenException(message);
        }
        if (event.getParticipantLimit()==event.getConfirmedRequests()){
            String message = "Достигнут лимит на участие в событии.";
            log.warn(message);
            throw new ForbiddenException(message);
        }
        User user = BeanFinder.findUserById(userId,userRepository);
        Request newRequest = Request.builder()
                .requester(user)
                .event(event)
                .build();
        if (!event.getRequestModeration()){
            newRequest.setStatus(RequestStatus.CONFIRMED);
        }
        Request request = requestRepository.save(newRequest);
        log.info("ЗАпрос на участие в событии id={} успешно добавлен.",eventId savedUser.getId());
        return RequestMapper.toRequestDto(request);
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
