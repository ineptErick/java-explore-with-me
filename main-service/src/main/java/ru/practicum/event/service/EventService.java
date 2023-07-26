package ru.practicum.event.service;


import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.enums.EventStateAction;
import ru.practicum.event.model.Event;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto newEvent);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventRequest updateEventByUser);

    Event updateEvent(Event updatedEvent, UpdateEventRequest updateEventRequest);
    void setEventStateByEventStateAction(Event event, EventStateAction eventStateAction);
    EventFullDto getFullEventById(Long userId, Long eventId);

    List<EventShortDto> getAlUsersEvents(Integer from, Integer size, Long userId);





    Event getEventById(Long eventId);
    void checkIfEvenDateCorrect(LocalDateTime evenDate);
    void checkIfEventCanBeUpdated(UpdateEventRequest updatedEven, Event oldEvent, User user);

}
