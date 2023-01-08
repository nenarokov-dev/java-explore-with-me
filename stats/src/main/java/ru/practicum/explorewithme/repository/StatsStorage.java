package ru.practicum.explorewithme.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;
import ru.practicum.explorewithme.repository.mapper.ViewStatsMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class StatsStorage {
    private final JdbcTemplate jdbcTemplate;

    public EndpointHit save(EndpointHit endpointHit) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String saveMessage = "INSERT INTO endpoint_hits (app,uri,ip,time_stamp) VALUES (?,?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(saveMessage, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, endpointHit.getApp());
            ps.setString(2, endpointHit.getUri());
            ps.setString(3, endpointHit.getIp());
            ps.setTimestamp(4, Timestamp.valueOf(endpointHit.getTimestamp()));
            return ps;
        }, keyHolder);
        HashMap<Object, Object> ff = new HashMap<>(Objects.requireNonNull(keyHolder.getKeys()));
        endpointHit.setId((Long) ff.get("id"));
        return endpointHit;
    }

    public List<ViewStats> getAllViews(String start, String end, String[] uris, Boolean unique) {
        List<ViewStats> stats = new ArrayList<>();
        if (unique) {
            if (uris != null) {
                for (String uri : uris) {
                    stats.add(jdbcTemplate.queryForObject(
                            "SELECT eh.app,eh.uri,count(DISTINCT eh.ip) AS hits " +
                                    "FROM endpoint_hits as eh " +
                                    "WHERE eh.uri LIKE '" + uri + "' " +
                                    "AND eh.time_stamp BETWEEN '" + start + "' " +
                                    "AND '" + end + "' " +
                                    "GROUP by eh.app,eh.uri;",
                            new ViewStatsMapper()));
                }
            } else {
                stats.add(jdbcTemplate.queryForObject(
                        "SELECT eh.app,eh.uri,count(DISTINCT eh.ip) AS hits " +
                                "FROM endpoint_hits as eh " +
                                "WHERE eh.time_stamp BETWEEN '" + start + "' " +
                                "AND '" + end + "' " +
                                "GROUP by eh.app,eh.uri;",
                        new ViewStatsMapper()));
            }
        } else {
            if (uris != null) {
                for (String uri : uris) {
                    stats.add(jdbcTemplate.queryForObject(
                            "SELECT eh.app,eh.uri,count(eh.ip) AS hits " +
                                    "FROM endpoint_hits as eh " +
                                    "WHERE eh.uri LIKE '" + uri + "' " +
                                    "AND eh.time_stamp BETWEEN '" + start + "' " +
                                    "AND '" + end + "' " +
                                    "GROUP by eh.app,eh.uri;",
                            new ViewStatsMapper()));
                }
            } else {
                stats.add(jdbcTemplate.queryForObject(
                        "SELECT eh.app,eh.uri,count(DISTINCT eh.ip) AS hits " +
                                "FROM endpoint_hits as eh " +
                                "WHERE eh.time_stamp BETWEEN '" + start + "' " +
                                "AND '" + end + "' " +
                                "GROUP by eh.app,eh.uri;",
                        new ViewStatsMapper()));
            }
        }
        return stats;
    }
}
