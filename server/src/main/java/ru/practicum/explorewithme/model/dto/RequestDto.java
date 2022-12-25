package ru.practicum.explorewithme.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.explorewithme.model.RequestStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class RequestDto {
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private RequestStatus status;
}