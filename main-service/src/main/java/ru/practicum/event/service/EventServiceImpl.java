package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ApiError.exception.ValidationException;
import ru.practicum.StatisticClientController;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.repository.EventRepository;

import ru.practicum.request.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.service.UsersService;

import java.time.LocalDateTime;


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
        if (LocalDateTime.now().plusHours(2).isAfter(newEvent.getEventDate())) {
            log.error("Некорректная дата начала мероприятия. (Меньше 2-х часов до начала).");
            throw new ValidationException("Некорректная дата начала мероприятия. (Меньше 2-х часов до начала).");
        }
        User user = usersService.getUserById(userId);
        CategoryDto categoryDto = categoryService.getCategoryById(newEvent.getCategory());
        Event event = eventRepository.save(EventMapper.INSTANT.toEvent(newEvent));
        event.setInitiator(user);
        event.setCategory(CategoryMapper.INSTANT.categoryDtoToCategory(categoryDto));
        log.debug("Пользователь с ID = {} создал мероприятие \"{}\". ID = {}.",
                userId, newEvent.getTitle(), event.getId());
        return EventMapper.INSTANT.toEventFullDto(event);
    }
}
