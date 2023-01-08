package ru.practicum.explorewithme.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.model.EndpointHit;

import java.util.Map;

@Service
@Slf4j
public class EventStatsClient extends BaseClient {

    @Autowired
    public EventStatsClient(@Value("${ewm-stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> hit(EndpointHit hit) {
        return post("/hit", hit);
    }

    public ResponseEntity<Object> getStats(String start, String end, String[] uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique", unique
        );
        StringBuilder stringBuilder = new StringBuilder();
        if (uris.length > 0) {
            for (String s : uris) {
                stringBuilder.append("uris=").append(s).append("&");
            }
        }
        return get("/stats?" + stringBuilder + "start={start}&end={end}&unique={unique}", null, parameters);
    }


}