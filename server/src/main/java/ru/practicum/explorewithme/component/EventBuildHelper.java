package ru.practicum.explorewithme.component;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.client.EventStatsClient;
import ru.practicum.explorewithme.model.event.Event;
import ru.practicum.explorewithme.model.event.dto.EventOutputDto;
import ru.practicum.explorewithme.model.event.dto.EventOutputShortDto;
import ru.practicum.explorewithme.model.event.mapper.EventMapper;
import ru.practicum.explorewithme.model.request.Request;
import ru.practicum.explorewithme.repository.RequestRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventBuildHelper {

    public static List<EventOutputDto> getEventOutputDto(List<Event> events, RequestRepository requestRepository,
                                                         EventStatsClient statsClient) {
        List<Long> eventsIds = new ArrayList<>();
        events.forEach(e -> eventsIds.add(e.getId()));
        List<Request> allRequests = requestRepository.findAll();
        HashMap<Long, Long> eventRequests = new HashMap<>();
        for (Long eventId : eventsIds) {
            Long requests = allRequests.stream().filter(e -> e.getEvent().getId().equals(eventId)).count();
            eventRequests.put(eventId, requests);
        }
        HashMap<Long, Long> eventViews = EventValuesCollector.getEventViews(eventsIds, statsClient);
        return events.stream()
                .map(e -> EventMapper.toEventOutputDtoFromEvent(e, eventRequests.get(e.getId()), eventViews.get(e.getId())))
                .collect(Collectors.toList());
    }

    public static List<EventOutputShortDto> getEventOutputShortDto(List<Event> events, RequestRepository requestRepository,
                                                                   EventStatsClient statsClient) {
        List<Long> eventsIds = new ArrayList<>();
        events.forEach(e -> eventsIds.add(e.getId()));
        List<Request> allRequests = requestRepository.findAll();
        HashMap<Long, Long> eventRequests = new HashMap<>();
        for (Long eventId : eventsIds) {
            Long requests = allRequests.stream().filter(e -> e.getEvent().getId().equals(eventId)).count();
            eventRequests.put(eventId, requests);
        }
        HashMap<Long, Long> eventViews = EventValuesCollector.getEventViews(eventsIds, statsClient);
        return events.stream()
                .map(e -> EventMapper.toEventOutputShortDto(e, eventRequests.get(e.getId()), eventViews.get(e.getId())))
                .collect(Collectors.toList());
    }
}
