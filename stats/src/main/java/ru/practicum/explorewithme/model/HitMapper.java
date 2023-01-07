package ru.practicum.explorewithme.model;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.component.DateTimeAdapter;

import java.time.LocalDateTime;

@Component
public class HitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .ip(endpointHitDto.getIp())
                .uri(endpointHitDto.getUri())
                .app(endpointHitDto.getApp())
                .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeAdapter.formatter))
                .build();
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .app(endpointHit.getApp())
                .timestamp(endpointHit.getTimestamp().format(DateTimeAdapter.formatter))
                .build();
    }

}