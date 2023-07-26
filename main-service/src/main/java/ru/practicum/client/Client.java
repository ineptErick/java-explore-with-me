package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.StatsClient;
import ru.practicum.dto.ViewStats;
import ru.practicum.event.dto.EventFullDto;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Client {

    private final StatsClient statisticClient;


   /* public List<EventShortDto> setViewsToEventsShortDto(List<EventShortDto> eventsShortDto) {
        Set<String> uris = new HashSet<>();
        for (EventShortDto eventShortDto : eventsShortDto) {
            uris.add("/events/" + eventShortDto.getId());
        }

        Map<Long, Long> views = getViewsForEvents(uris);

        eventsShortDto.forEach(e -> e.setViews(views.get(e.getId())));

        return eventsShortDto;
    }*/

    public EventFullDto setViewsToEventFullDto(EventFullDto eventFullDto) {
        List<ViewStats> stats = getViewsForEvent(eventFullDto.getId());

        if (stats != null) {
            eventFullDto.setViews(stats.get(0).getHits());
        }

        return eventFullDto;
    }

    private List<ViewStats> getViewsForEvent(Long eventId) {
        Set<String> uri = new HashSet<>();
        uri.add("/events/" + eventId);
        return statisticClient.getAllStats(uri);
    }
}
