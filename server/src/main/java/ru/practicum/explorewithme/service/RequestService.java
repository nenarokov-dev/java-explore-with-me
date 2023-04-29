package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.component.BeanFinder;
import ru.practicum.explorewithme.component.EventValuesCollector;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.exceptions.DuplicateException;
import ru.practicum.explorewithme.exceptions.ForbiddenException;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.EventState;
import ru.practicum.explorewithme.model.request.Request;
import ru.practicum.explorewithme.model.request.RequestStatus;
import ru.practicum.explorewithme.model.request.dto.RequestDto;
import ru.practicum.explorewithme.model.request.mapper.RequestMapper;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.RequestRepository;
import ru.practicum.explorewithme.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class RequestService {

    private UserRepository userRepository;
    private EventRepository eventRepository;
    private RequestRepository requestRepository;

    public RequestDto add(Long userId, Long eventId) {
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId) != null) {
            String message = "Пользователь id=" + userId + " уже отправил запрос на участие в событии id=" + eventId + ".";
            log.warn(message);
            throw new DuplicateException(message);
        }
        Event event = BeanFinder.findEventById(eventId, eventRepository);
        if (event.getInitiator().getId().equals(userId)) {
            String message = "Инициатор не может создавать запросы на участие в событии.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            String message = "Нельзя принимать участие в неопубликованном событии.";
            log.warn(message);
            throw new ForbiddenException(message);
        }
        Long confirmedRequests = EventValuesCollector.getConfirmedRequest(eventId, requestRepository);
        if (event.getParticipantLimit() == confirmedRequests) {
            String message = "Достигнут лимит на участие в событии.";
            log.warn(message);
            throw new ForbiddenException(message);
        }
        User user = BeanFinder.findUserById(userId, userRepository);
        Request newRequest = Request.builder()
                .requester(user)
                .event(event)
                .build();
        if (!event.getRequestModeration()) {
            newRequest.setStatus(RequestStatus.CONFIRMED);
        }
        Request request = requestRepository.save(newRequest);
        log.info("Запрос на участие в событии id={} успешно добавлен.", request.getId());
        return RequestMapper.toRequestDto(request);
    }

    public List<RequestDto> getAll(Long userId) {
        List<RequestDto> requests = requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        log.info("Запросы пользователя id={} на участие в событиях успешно получены.", userId);
        return requests;
    }

    public RequestDto rejectByRequester(Long userId, Long requestId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        Request request = BeanFinder.findRequestById(requestId, requestRepository);
        if (!request.getRequester().equals(user)) {
            String message = "Отменить участие в собитии может только автор запроса.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        request.setStatus(RequestStatus.CANCELED);
        Request canceledRequest = requestRepository.save(request);
        log.info("Запрос на участие в событии id={} успешно отменен.", canceledRequest.getId());
        return RequestMapper.toRequestDto(canceledRequest);
    }

    public List<RequestDto> getAllByInitiator(Long userId, Long eventId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        Event event = BeanFinder.findEventById(eventId, eventRepository);
        if (!event.getInitiator().equals(user)) {
            String message = "Информация о запросах на участие в событии доступна только его инициатору.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        List<RequestDto> requests = requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        log.info("Информация о запросах на участие в событии id={}, созданного пользователем id={} успешно получена.",
                eventId, userId);
        return requests;
    }

    public RequestDto confirmRequest(Long userId, Long eventId, Long reqId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        Event event = BeanFinder.findEventById(eventId, eventRepository);
        Request request = BeanFinder.findRequestById(reqId, requestRepository);
        if (!event.getInitiator().equals(user)) {
            String message = "Подтверждать заявки на участие в событии может только его инициатор.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        if (event.getRequestModeration().equals(false)) {
            return confirmRequest(request, eventId);
        }
        Long confirmedRequests = EventValuesCollector.getConfirmedRequest(eventId, requestRepository);
        if (confirmedRequests == event.getParticipantLimit()) {
            String message = "Невозможно подтвердить заявку на участие.Достигнут максимум участников.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            String message = "Запрос уже был подтвержден.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        RequestDto requestDto = confirmRequest(request, eventId);
        if (confirmedRequests == event.getParticipantLimit()) {
            List<Long> unconfirmedRequestsId = requestRepository
                    .findRequestsIdByStatusAndEventId(RequestStatus.PENDING, eventId);
            for (Long requestId : unconfirmedRequestsId) {
                Request requestForCancel = requestRepository.getReferenceById(requestId);
                requestForCancel.setStatus(RequestStatus.CANCELED);
                requestRepository.save(requestForCancel);
            }
        }
        return requestDto;
    }

    public RequestDto rejectRequestByEventInitiator(Long userId, Long eventId, Long reqId) {
        User user = BeanFinder.findUserById(userId, userRepository);
        Event event = BeanFinder.findEventById(eventId, eventRepository);
        Request request = BeanFinder.findRequestById(reqId, requestRepository);
        if (!event.getInitiator().equals(user)) {
            String message = "Отклонять заявки на участие в событии может только его инициатор.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        if (request.getStatus().equals(RequestStatus.REJECTED) ||
                request.getStatus().equals(RequestStatus.CANCELED)) {
            String message = "Заявка на участие в событии уже отклонена.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        request.setStatus(RequestStatus.REJECTED);
        Request rejectedRequest = requestRepository.save(request);
        return RequestMapper.toRequestDto(rejectedRequest);
    }

    private RequestDto confirmRequest(Request request, Long eventId) {
        request.setStatus(RequestStatus.CONFIRMED);
        Request confirmedRequest = requestRepository.save(request);
        log.info("Запрос id={} на участие в событии id={} успешно подтвержден.", request.getId(), eventId);
        return RequestMapper.toRequestDto(confirmedRequest);
    }

}
