package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ApiError.exception.BadRequestException;
import ru.practicum.ApiError.exception.ConflictException;
import ru.practicum.ApiError.exception.NotFoundException;
import ru.practicum.ApiError.exception.ValidationException;
import ru.practicum.StatisticClientController;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.EventStateAction;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.repository.EventRepository;

import ru.practicum.request.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.service.UsersService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final CategoryService categoryService;

    private final UsersService usersService;

    private final StatisticClientController statisticClientController;

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
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventRequest updateEventByUser) {
        log.info("Пользователь с ID = {} обновляет мероприятие с ID = {}.", userId, eventId);
        User user = usersService.getUserById(userId);
        categoryService.isCategoryPresent(updateEventByUser.getCategory());
        Event eventForUpdate = getEventById(eventId);
        checkIfEventCanBeUpdated(updateEventByUser, eventForUpdate, user);
        return EventMapper.INSTANT.toEventFullDto(
                eventRepository.save(
                        updateEvent(eventForUpdate, updateEventByUser)));
    }

    @Override
    public EventFullDto getFullEventById(Long userId, Long eventId) {
        log.info("Пользователь с ID = {} запросил информации о мероприятии с ID = {}.", userId, eventId);
        usersService.isUserPresent(eventId);
        return EventMapper.INSTANT.toEventFullDto(getEventById(eventId));
    }

    @Override
    public List<EventShortDto> getAlUsersEvents(Integer from, Integer size, Long userId) {
        Integer page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        log.info("Выгрузка списка мероприятий для пользователя с ID = {} с параметрами: size={}, from={}.",userId, size, page);
        Page<Event> pageEvents = eventRepository.getAllEventsByUserId(userId, pageRequest);
        List<Event> requests = pageEvents.getContent();
        List<EventShortDto> requestsDto = EventMapper.INSTANT.toEventShortDto(requests);
        return requestsDto;
    }

    @Override
    public Event updateEvent(Event updatedEvent, UpdateEventRequest updateEventRequest) {
        Optional.ofNullable(updateEventRequest.getAnnotation()).ifPresent(updatedEvent::setAnnotation);
        Optional.ofNullable(updateEventRequest.getCategory()).ifPresent(
                c -> updatedEvent.setCategory(categoryService.getCategoryModelById(c)));
        Optional.ofNullable(updateEventRequest.getDescription()).ifPresent(updatedEvent::setDescription);
        Optional.ofNullable(updateEventRequest.getEventDate()).ifPresent(updatedEvent::setEventDate);
        Optional.ofNullable(updateEventRequest.getLocation().getLat()).ifPresent(updatedEvent::setLat);
        Optional.ofNullable(updateEventRequest.getLocation().getLon()).ifPresent(updatedEvent::setLon);
        Optional.ofNullable(updateEventRequest.getPaid()).ifPresent(updatedEvent::setPaid);
        Optional.ofNullable(updateEventRequest.getParticipantLimit()).ifPresent(updatedEvent::setParticipantLimit);
        Optional.ofNullable(updateEventRequest.getRequestModeration()).ifPresent(updatedEvent::setRequestModeration);
        Optional.ofNullable(updateEventRequest.getStateAction()).ifPresent(
                s -> setEventStateByEventStateAction(updatedEvent, updateEventRequest.getStateAction())
        );
        Optional.ofNullable(updateEventRequest.getTitle()).ifPresent(updateEventRequest::setTitle);
        return updatedEvent;
    }










    @Override
    public void checkIfEventCanBeUpdated(UpdateEventRequest updatedEven, Event oldEvent, User user) {
        if (!oldEvent.getInitiator().getId().equals(user.getId())) {
            log.error("Только инициатор или администратор могут менять мероприятие.");
            throw new BadRequestException("Только инициатор или администратор могут менять мероприятие.");
        }
        checkIfEvenDateCorrect(updatedEven.getEventDate());
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

/*    @Override
    public void checkIfEvenEventChangedByInitiator(Event event, User user) {
        if (!event.getInitiator().getId().equals(user.getId())) {
            log.error("Только инициатор или администратор могут менять мероприятие.");
            throw new BadRequestException("Только инициатор или администратор могут менять мероприятие.");
        }
    }*/

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
}
