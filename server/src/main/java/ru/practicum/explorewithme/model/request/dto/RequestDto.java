package ru.practicum.explorewithme.model.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.model.request.RequestStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class RequestDto {

    private Long id;
    private Long event;
    private Long requester;
    private LocalDateTime created;
    private RequestStatus status;
}
