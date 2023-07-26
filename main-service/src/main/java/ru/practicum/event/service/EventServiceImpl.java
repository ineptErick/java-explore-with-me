package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ApiError.exception.BadRequestException;
import ru.practicum.ApiError.exception.ConflictException;
import ru.practicum.ApiError.exception.NotFoundException;
import ru.practicum.ApiError.exception.ValidationException;
//import ru.practicum.StatisticClientController;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.comparator.EventShortSortByDate;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.EventStateAction;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;

import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestMapper;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.service.UsersService;

import java.time.LocalDateTime;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final CategoryService categoryService;

    private final UsersService usersService;

//    private final StatisticClientController statisticClientController;

    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEvent) {
        log.info("Пользователь с ID = {} создает мероприятие \"{}\".", userId, newEvent.getTitle());
        checkIfEvenDateCorrect(newEvent.getEventDate());
        User user = usersService.getUserById(userId);
        CategoryDto categoryDto = categoryService.getCategoryById(newEvent.getCategory());
        Event event = eventRepository.save(EventMapper.INSTANT.toEvent(newEvent));
        event.setInitiator(user);
        event.setCategory(CategoryMapper.INSTANT.categoryDtoToCategory(categoryDto));
        log.debug("Пользователь с ID = {} создал мероприятие \"{}\". ID = {}.",
                userId, newEvent.getTitle(), event.getId());
        return EventMapper.INSTANT.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventRequest updateEvent, Boolean isAdmin, Boolean isOwner) {
        if (updateEvent.getCategory() != null) {
            categoryService.isCategoryPresent(updateEvent.getCategory());
        }
        Event eventForUpdate = getEventById(eventId);
        if (isOwner) {
            log.info("Пользователь с ID = {} обновляет мероприятие с ID = {}.", userId, eventId);
            User user = usersService.getUserById(userId);
            checkIfEventCanBeUpdated(updateEvent, eventForUpdate, user);
            log.debug("Пользователь с ID = {} обновил мероприятие с ID = {}.", userId, eventId);
        } else {
            log.info("Администратор обновляет мероприятие с ID = {}.", eventId);
            if (updateEvent.getEventDate() != null) {
                checkIfEvenDateCorrect(updateEvent.getEventDate());
            }
            log.debug("Администратор обновил мероприятие с ID = {}.", eventId);
        }
        return EventMapper.INSTANT.toEventFullDto(
                eventRepository.save(
                        updateEvent(eventForUpdate, updateEvent)));
    }

    @Override
    public EventFullDto getFullEventById(Long userId, Long eventId) {
        log.info("Пользователь с ID = {} запросил информации о мероприятии с ID = {}.", userId, eventId);
        usersService.isUserPresent(userId);
        return EventMapper.INSTANT.toEventFullDto(getEventById(eventId));
    }

    @Override
    public List<EventShortDto> getAllUsersEvents(Integer from, Integer size, Long userId) {
        Integer page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        log.info("Выгрузка списка мероприятий для пользователя с ID = {} с параметрами: size={}, from={}.",userId, size, page);
        Page<Event> pageEvents = eventRepository.getAllEventsByUserId(userId, pageRequest);
        List<Event> requests = pageEvents.getContent();
        List<EventShortDto> requestsDto = EventMapper.INSTANT.toEventShortDto(requests);
        return requestsDto;
    }

    @Override
    public List<ParticipationRequestDto> getRequestsOnEvent(Long userId, Long eventId) {
        log.info("Выгрузка списка запросов на участие в мероприятии с ID = {}.", eventId);
        usersService.isUserPresent(userId);
        Event event = getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("Только организатор может просматривать список запросов на участие.");
        } else {
            List<Request> request = requestRepository.findAllByEventId(eventId);
            return RequestMapper.INSTANT.toParticipationRequestDto(request);
        }
    }

    //TODO проверить работу метода
    @Override
    @Transactional
    public EventRequestStatusUpdateResult processWithEventsRequests(
            Long userId, Long eventId, EventRequestStatusUpdateRequest requests) {
        log.info("Пользовать с ID = {} обрабатывает заявки на мероприятие с ID = {}.", userId, eventId);
        usersService.isUserPresent(userId);
        Event event = getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("Только организатор может обрабатывать список запросов на участие.");
        }
        if (!event.getRequestModeration()) {
            throw new BadRequestException("Запросы не требуют модерации. Пре-модерация отключена.");
        }
        if (event.getParticipantLimit() == 0) {
            throw new BadRequestException("Запросы не требуют модерации. Лимит на участников не установлен.");
        }
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        List<Request> requestsList = requestRepository.findAllByIdInAndStatusEquals(
                requests.getRequestIds(), RequestStatus.PENDING);
        if (requests.getStatus().equals(RequestStatus.CONFIRMED)) {
            int freePlaces = event.getParticipantLimit() - event.getParticipants().size();
            int count = 0;
            for (Request request: requestsList) {
                checkRequestBeforeUpdate(event, request);
                log.info("Обработка запроса с ID = {}.", request.getId());
                if (freePlaces != count) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    eventRequestStatusUpdateResult.getConfirmedRequests()
                            .add(RequestMapper.INSTANT.toParticipationRequestDto(request));
                    count++;
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    eventRequestStatusUpdateResult.getRejectedRequests()
                            .add(RequestMapper.INSTANT.toParticipationRequestDto(request));
                }
                log.debug("Статус запроса с ID = {} на \"{}\".", request.getId(), request.getStatus());
            }
        } else {
            for (Request request: requestsList) {
                checkRequestBeforeUpdate(event, request);
                log.info("Обработка запроса с ID = {}.", request.getId());
                request.setStatus(RequestStatus.REJECTED);
                eventRequestStatusUpdateResult.getRejectedRequests()
                        .add(RequestMapper.INSTANT.toParticipationRequestDto(request));
                log.debug("Статус запроса с ID = {} на \"{}\".", request.getId(), RequestStatus.REJECTED);
            }
        }
        requestRepository.saveAll(requestsList);
        return eventRequestStatusUpdateResult;
    }


    void checkRequestBeforeUpdate(Event event, Request request) {
        if (!request.getEvent().getId().equals(event.getId())) {
            throw new BadRequestException("Запрос для другого мероприятия.");
        }
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            throw new BadRequestException("Статус запроса отличен от PENDING.");
        }
    }


    @Override
    public Event updateEvent(Event updatedEvent, UpdateEventRequest updateEventRequest) {
        Optional.ofNullable(updateEventRequest.getAnnotation()).ifPresent(updatedEvent::setAnnotation);
        Optional.ofNullable(updateEventRequest.getCategory()).ifPresent(
                c -> updatedEvent.setCategory(categoryService.getCategoryModelById(c)));
        Optional.ofNullable(updateEventRequest.getDescription()).ifPresent(updatedEvent::setDescription);
        Optional.ofNullable(updateEventRequest.getEventDate()).ifPresent(updatedEvent::setEventDate);
        if (updateEventRequest.getLocation() != null) {
            if (updateEventRequest.getLocation().getLat() != null) {
                updatedEvent.setLat(updateEventRequest.getLocation().getLat());
            }
            if (updateEventRequest.getLocation().getLon() != null) {
                updatedEvent.setLon(updateEventRequest.getLocation().getLon());
            }
        }
        Optional.ofNullable(updateEventRequest.getPaid()).ifPresent(updatedEvent::setPaid);
        Optional.ofNullable(updateEventRequest.getParticipantLimit()).ifPresent(updatedEvent::setParticipantLimit);
        Optional.ofNullable(updateEventRequest.getRequestModeration()).ifPresent(updatedEvent::setRequestModeration);
        Optional.ofNullable(updateEventRequest.getStateAction()).ifPresent(
                s -> setEventStateByEventStateAction(updatedEvent, updateEventRequest.getStateAction())
        );
        Optional.ofNullable(updateEventRequest.getTitle()).ifPresent(updatedEvent::setTitle);
        return updatedEvent;
    }

    @Override
    public void checkIfEventCanBeUpdated(UpdateEventRequest updatedEven, Event oldEvent, User user) {
        if (!oldEvent.getInitiator().getId().equals(user.getId())) {
            log.error("Только инициатор или администратор могут менять мероприятие.");
            throw new BadRequestException("Только инициатор или администратор могут менять мероприятие.");
        }
        if (updatedEven.getEventDate() != null) {
            checkIfEvenDateCorrect(updatedEven.getEventDate());
        }
        if (oldEvent.getState().equals(EventState.PUBLISHED)) {
            log.error("Только мероприятия со статусом PENDING или CANCELED могут быть изменены.");
            throw new ConflictException("Только мероприятия со статусом PENDING или CANCELED могут быть изменены.");
        }
    }


    @Override
    public void checkIfEvenDateCorrect(LocalDateTime evenDate) {
        if (LocalDateTime.now().plusHours(2).isAfter(evenDate)) {
            log.error("Некорректная дата начала мероприятия. (Меньше 2-х часов до начала).");
            throw new ValidationException("Некорректная дата начала мероприятия. (Меньше 2-х часов до начала).");
        }
    }

    @Override
    public void setEventStateByEventStateAction(Event event, EventStateAction eventStateAction) {
        switch (eventStateAction) {
            case PUBLISH_EVENT:
                event.setState(EventState.PUBLISHED);
                break;
            case SEND_TO_REVIEW:
                event.setState(EventState.PENDING);
                break;
            default:
                event.setState(EventState.CANCELED);
                break;
        }
    }




    @Override
    public Event getEventById(Long eventId) {
        log.info("Получение мероприятия по ID = {}.", eventId);
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Мероприятие с ID = " + eventId + " не найдено.")
        );
    }

    @Override
    public void isEventIsPresent(Long eventId) {
        eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Мероприятие с ID = " + eventId + " не найдено.")
        );
    }

    @Override
    public List<EventFullDto> getAllEventsByAdmin(
            Set<Long> users, Set<EventState> states, Set<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.info("Выгрузка списка мероприятий администратором с параметрами: " +
                "users = {}, sates = {}, categories = {}, rangeStart = {}, rangeEnd = {}, from = {}, size = {}.",
                users, states, categories, rangeStart, rangeEnd, from, size);
        BooleanExpression byUsers;
        BooleanExpression byStates;
        BooleanExpression byCategories;
        BooleanExpression byDate = QEvent.event.eventDate.between(rangeStart, rangeEnd);
        if (users.isEmpty()) {
            byUsers = QEvent.event.initiator.id.notIn(users);
        } else {
            byUsers = QEvent.event.initiator.id.in(users);
        }
        if (states.isEmpty()) {
            byStates = QEvent.event.state.notIn(states);
        } else {
            byStates = QEvent.event.state.in(states);
        }
        if (categories.isEmpty()) {
            byCategories = QEvent.event.category.id.notIn(categories);
        } else {
            byCategories = QEvent.event.category.id.in(categories);
        }
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        Iterable<Event> foundEvents = eventRepository.findAll(
                byUsers.and(byStates).and(byCategories).and(byDate), pageRequest);
        return EventMapper.INSTANT.iterableToList(foundEvents);
    }

    @Override
    public List<EventShortDto> getEventsByPublic(
            String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            Boolean onlyAvailable, EventSort sort, Integer from, Integer size) {
        log.info("Выгрузка списка мероприятий. Публичный API с параметрами: " +
                        "text = {}, categories = {}, paid = {}, rangeStart = {}, rangeEnd = {}, onlyAvailable = {}, " +
                        "sort = {}, from = {}, size = {}.",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        BooleanExpression byPaid = QEvent.event.paid.eq(paid);
        BooleanExpression byDate = QEvent.event.eventDate.between(rangeStart, rangeEnd);
        BooleanExpression byCategories;
        BooleanExpression byAnnotation;
        BooleanExpression byDescription;
        BooleanExpression byState = QEvent.event.state.eq(EventState.PUBLISHED);
        int page = from / size;
        String sorting;
        if (sort.equals(EventSort.EVENT_DATE)) {
            sorting = "eventDate";
        } else {
            sorting = "views";
        }
        Pageable pageable = PageRequest.of(page, size);
        if (categories.isEmpty()) {
            byCategories = QEvent.event.category.id.notIn(categories);
        } else {
            byCategories = QEvent.event.category.id.in(categories);
        }
        Page<Event> eventsPage;
        if (text != null) {
            byAnnotation = QEvent.event.annotation.likeIgnoreCase(text);
            byDescription = QEvent.event.description.likeIgnoreCase(text);
            eventsPage = eventRepository.findAll(
                    byAnnotation.and(byDescription).and(byPaid).and(byDate).and(byCategories).and(byState), pageable);
        } else {
            eventsPage = eventRepository.findAll(
                    byPaid.and(byDate).and(byCategories).and(byState), pageable);
        }
        List<Event> events = eventsPage.getContent();
        if (onlyAvailable) {
            events.removeIf(event -> event.getParticipants().size() == event.getParticipantLimit());
        }
//        List<EventShortDto> result = EventMapper.INSTANT.toEventShortDto(events);
//        List<EventShortDto> sortedResult = result.stream()
//                .sorted(Comparator.comparing(EventShortSortByDate, EventShortDto::getEventDate).collect(Collectors.toList());
        return EventMapper.INSTANT.toEventShortDto(events);
    }

}
