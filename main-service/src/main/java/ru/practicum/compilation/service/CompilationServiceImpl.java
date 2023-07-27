package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ApiError.exception.NotFoundException;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventService eventService;


    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilation) {
        log.info("Администратор создает новую подборку \"{}\".", newCompilation.getTitle());
        List<Event> events = eventService.getEventByIds(newCompilation.getEvents());
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilation, events));
        log.debug("Администратор создал новую подборку \"{}\" с ID = {}.", compilation.getTitle(), compilation.getId());
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilationById(Long compId, UpdateCompilationRequest updatedCompilation) {
        log.info("Администратор обновляет подборку с ID = {}.", compId);
        Compilation compilationOld = getCompilationById(compId);
        List<Event> events = eventService.getEventByIds(updatedCompilation.getEvents());
        Optional.ofNullable(updatedCompilation.getPinned()).ifPresent(compilationOld::setPinned);
        Optional.ofNullable(updatedCompilation.getTitle()).ifPresent(compilationOld::setTitle);
        if (events != null) {
            compilationOld.setEvents(events);
        }
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(compilationOld, events));
        log.debug("Администратор обновил подборку \"{}\" с ID = {}.", compilation.getTitle(), compilation.getId());
            return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public Compilation getCompilationById(Long compId) {
        log.info("Получение подборки по ID = {}.", compId);
        return compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Подборка с ID = " + compId + " не найдена.")
        );
    }

    @Override
    @Transactional
    public void deleteComplication(Long compId) {
        log.debug("Администратор удаляет подборку с ID = {}.", compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto getCompilationByIdPublic(Long compId) {
        return CompilationMapper.toCompilationDto(getCompilationById(compId));
    }

    @Override
    public List<CompilationDto> getComplicationsPublic(Boolean pinned, Integer from, Integer size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        log.info("Выгрузка списка подборок с параметрами: pinned = {}, size={}, from={}.",pinned, size, page);
        Page<Compilation> pageCompilation = compilationRepository.findAllByPinned(pinned, pageRequest);
        List<Compilation> requests = pageCompilation.getContent();
        return CompilationMapper.toCompilationDto(requests);
    }

}
