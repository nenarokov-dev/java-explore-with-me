package ru.practicum.explorewithme.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class EndpointHit {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
