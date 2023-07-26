package ru.practicum.event.service;


import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.EventStateAction;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto newEvent);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventRequest updateEventByUser, Boolean isAdmin, Boolean isOwner);

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

    List<EventFullDto> getAllEventsByAdmin(Set<Long> users, Set<EventState> states, Set<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<EventShortDto> getEventsByPublic(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, Integer from, Integer size);

    EventFullDto getEventByIdPubic(Long eventId);

    List<Event> getEventByIds(List<Long> events);
}
