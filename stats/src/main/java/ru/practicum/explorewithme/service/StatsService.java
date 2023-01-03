package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.model.EndpointHit;
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

    public String saveHit(EndpointHit endpointHit) {
        EndpointHit hit = statsStorage.save(endpointHit);
        log.info("Запрос к сервису {} через uri={} был успешно сохранен.",hit.getApp(),hit.getUri());
        return "Информация сохранена.";
    }

    public List<ViewStats> getAllViews(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        String[] urisDecoded = Arrays.stream(uris)
                .map(e-> URLDecoder.decode(e, StandardCharsets.UTF_8))
                .toArray(String[]::new);
        List<ViewStats> stats = statsStorage.getAllViews(start,end,urisDecoded,unique);
        log.info("Статистика по сервисам {} успешно составлена.",Arrays.toString(uris));
        return stats;
    }
    //private final Pagination<UserDto> pagination;


}
