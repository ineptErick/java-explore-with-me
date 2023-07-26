package ru.practicum.event.comparator;

import ru.practicum.event.dto.EventShortDto;

import java.util.Comparator;

public class EventShortSortByDate implements Comparator<EventShortDto> {


    @Override
    public int compare(EventShortDto o1, EventShortDto o2) {
        if (o1.getEventDate().equals(o2.getEventDate())) {
            if (o1.getViews().equals(o2.getViews())) {
                return o1.getId().compareTo(o2.getId());
            } else {
                return o1.getViews().compareTo(o2.getViews());
            }
        } else {
            return o1.getEventDate().compareTo(o2.getEventDate());
        }
    }
}
