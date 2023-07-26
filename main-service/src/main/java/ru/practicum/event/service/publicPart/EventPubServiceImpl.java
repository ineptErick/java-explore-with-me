package ru.practicum.event.service.publicPart;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ApiError.exception.NotFoundException;
import ru.practicum.StatsClient;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPubServiceImpl implements EventPubService {

    private final EventRepository eventRepository;

    private final StatsClient statisticClient;

    @Override
    public List<EventShortDto> getEventsByPublic(
            String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            Boolean onlyAvailable, EventSort sort, Integer from, Integer size, HttpServletRequest request) {
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
            byAnnotation = QEvent.event.annotation.likeIgnoreCase("%" + text + "%");
            byDescription = QEvent.event.description.likeIgnoreCase("%" + text + "%");
            eventsPage = eventRepository.findAll(
                    byAnnotation.or(byDescription).and(byPaid).and(byDate).and(byCategories).and(byState), pageable);
        } else {
            eventsPage = eventRepository.findAll(
                    byPaid.and(byDate).and(byCategories).and(byState), pageable);
        }
        List<Event> events = eventsPage.getContent();
        if (onlyAvailable) {
            events.removeIf(event -> event.getParticipants().size() == event.getParticipantLimit());
        }
        statisticClient.createHit(request.getRequestURI(), request.getRemoteAddr());
        return EventMapper.INSTANT.toEventShortDto(events);
    }

    @Override
    public EventFullDto getEventByIdPubic(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findFirstByIdAndState(eventId, EventState.PUBLISHED);
        if (event != null) {
            statisticClient.createHit(request.getRequestURI(), request.getRemoteAddr());
            return EventMapper.INSTANT.toEventFullDto(event);
        } else {
            throw new NotFoundException("Мероприятие с ID = " + eventId + " не найдено.");
        }
    }

}
