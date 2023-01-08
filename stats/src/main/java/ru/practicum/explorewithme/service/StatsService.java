package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.EndpointHitDto;
import ru.practicum.explorewithme.model.HitMapper;
import ru.practicum.explorewithme.model.ViewStats;
import ru.practicum.explorewithme.repository.StatsStorage;

import java.util.Arrays;
import java.util.Collections;
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
        try {
            List<ViewStats> stats = statsStorage.getAllViews(start, end, uris, unique);
            log.info("Статистика по сервисам {} успешно составлена.", Arrays.toString(uris));
            return stats;
        } catch (IncorrectResultSizeDataAccessException e) {
            return Collections.emptyList();
        }

    }
    //private final Pagination<UserDto> pagination;


}
