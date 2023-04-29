package ru.practicum.explorewithme.component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.client.EventStatsClient;
import ru.practicum.explorewithme.model.ViewStats;
import ru.practicum.explorewithme.model.request.RequestStatus;
import ru.practicum.explorewithme.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Component
@AllArgsConstructor
public class EventValuesCollector {

    public static final String URI = "/events/";
    private static final String start = LocalDateTime
            .of(2000, 1, 1, 1, 0, 0, 0)
            .format(DateTimeAdapter.formatter);
    private static final String end = LocalDateTime.now().format(DateTimeAdapter.formatter);

    public static Long getConfirmedRequest(Long eventId, RequestRepository requestRepository) {
        return requestRepository.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
    }

    public static HashMap<Long, Long> getEventViews(List<Long> ids, EventStatsClient statsClient) {
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls().setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        HashMap<Long, String> uriStorage = new HashMap<>();
        HashMap<Long, Long> eventsHits = new HashMap<>();
        ids.forEach(e -> uriStorage.put(e, URI + e));
        ResponseEntity<Object> objectResponseEntity = statsClient
                .getStats(start, end, uriStorage.values(), false);
        if (objectResponseEntity.getStatusCode() == HttpStatus.OK) {
            String responseJson = gson.toJson(objectResponseEntity.getBody());
            List<ViewStats> stats = gson.fromJson(responseJson,
                    new TypeToken<List<ViewStats>>() {
                    }.getType());
            if (!stats.isEmpty()) {
                for (Long eventId : uriStorage.keySet()) {
                    Long hits = 0L;
                    for (ViewStats viewStats : stats) {
                        if (viewStats.getUri().equals(uriStorage.get(eventId))) {
                            hits = viewStats.getHits();
                            break;
                        }
                    }
                    eventsHits.put(eventId, hits);
                }
            }
        }
        return eventsHits;
    }
}