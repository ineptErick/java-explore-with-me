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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {

    //Юсер
    EventFullDto createEvent(Long userId, NewEventDto newEvent);

    //Админ
    //Юсер
    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventRequest updateEventByUser, Boolean isAdmin, Boolean isOwner);

    //Админ
    //Юсер
    Event updateEvent(Event updatedEvent, UpdateEventRequest updateEventRequest, Boolean isAdmin);

    //Админ
    //Юсер
    void setEventStateByEventStateAction(Event event, EventStateAction eventStateAction);

    //Юсер
    EventFullDto getFullEventById(Long userId, Long eventId);

    //Юсер
    List<EventShortDto> getAllUsersEvents(Integer from, Integer size, Long userId);

    //Юсер
    List<ParticipationRequestDto> getRequestsOnEvent(Long userId, Long eventId);

    //Юсер
    EventRequestStatusUpdateResult processWithEventsRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest requests);





    Event getEventById(Long eventId);

    void checkIfEvenDateCorrect(LocalDateTime evenDate);

    void checkIfEventCanBeUpdated(UpdateEventRequest updatedEven, Event oldEvent, User user);

    void isEventIsPresent(Long eventId);

    //Админ
    List<EventFullDto> getAllEventsByAdmin(Set<Long> users, Set<EventState> states, Set<Long> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    //Паблик
    List<EventShortDto> getEventsByPublic(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, Integer from, Integer size, HttpServletRequest request);

    //Паблик
    EventFullDto getEventByIdPubic(Long eventId, HttpServletRequest request);

    List<Event> getEventByIds(List<Long> events);
}
