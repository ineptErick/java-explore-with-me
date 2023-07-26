package ru.practicum.event.service;


import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.enums.EventStateAction;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto newEvent);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventRequest updateEventByUser);

    Event updateEvent(Event updatedEvent, UpdateEventRequest updateEventRequest);
    void setEventStateByEventStateAction(Event event, EventStateAction eventStateAction);
    EventFullDto getFullEventById(Long userId, Long eventId);

    List<EventShortDto> getAllUsersEvents(Integer from, Integer size, Long userId);

    List<ParticipationRequestDto> getRequestsOnEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult processWithEventsRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest requests);





    Event getEventById(Long eventId);
    void checkIfEvenDateCorrect(LocalDateTime evenDate);
    void checkIfEventCanBeUpdated(UpdateEventRequest updatedEven, Event oldEvent, User user);

    void isEventIsPresent(Long eventId);

}
