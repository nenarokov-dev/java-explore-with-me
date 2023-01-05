package ru.practicum.explorewithme.controller.request;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.model.request.dto.RequestDto;
import ru.practicum.explorewithme.service.RequestService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@AllArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/users")
public class RequestControllerPrivate {

    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    public RequestDto add(@PathVariable Long userId, @RequestParam Long eventId) {
        return requestService.add(userId,eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<RequestDto> getAllByRequester(@PathVariable Long userId) {
        return requestService.get(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestService.rejectByRequester(userId,requestId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getAllByInitiator(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.getAllByInitiator(userId,eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmByInitiator(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @PathVariable Long reqId) {
        return requestService.confirmRequest(userId,eventId,reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectByInitiator(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @PathVariable Long reqId) {
        return requestService.rejectRequestByEventInitiator(userId,eventId,reqId);
    }


}
