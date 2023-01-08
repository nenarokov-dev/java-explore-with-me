package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.component.DateTimeAdapter;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.EndpointHitDto;
import ru.practicum.explorewithme.model.HitMapper;
import ru.practicum.explorewithme.model.ViewStats;
import ru.practicum.explorewithme.repository.StatsStorage;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class StatsService {

    private final StatsStorage statsStorage;

    public EndpointHitDto saveHit(EndpointHitDto endpointHit) {
        EndpointHit hit = statsStorage.save(HitMapper.toEndpointHit(endpointHit));
        log.info("Запрос к сервису {} через uri={} был успешно сохранен.", hit.getApp(), hit.getUri());
        return HitMapper.toEndpointHitDto(hit);
    }

    public List<ViewStats> getAllViews(String start, String end, String[] uris, Boolean unique) {
        String[] urisDecoded = Arrays.stream(uris)
                .map(e -> URLDecoder.decode(e, StandardCharsets.UTF_8))
                .toArray(String[]::new);
        List<ViewStats> stats = statsStorage.getAllViews(LocalDateTime.parse(start, DateTimeAdapter.formatter),
                LocalDateTime.parse(end, DateTimeAdapter.formatter), urisDecoded, unique);
        log.info("Статистика по сервисам {} успешно составлена.", Arrays.toString(uris));
        return stats;
    }
    //private final Pagination<UserDto> pagination;


}
