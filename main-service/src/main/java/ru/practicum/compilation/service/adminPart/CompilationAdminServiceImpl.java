package ru.practicum.compilation.service.adminPart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.utils.CompilationUtils;
import ru.practicum.event.model.Event;
import ru.practicum.event.utils.EventUtils;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationAdminServiceImpl implements CompilationAdminService {

    private final CompilationRepository compilationRepository;

    private final EventUtils eventUtils;

    private final CompilationUtils compilationUtils;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilation) {
        log.info("Администратор создает новую подборку \"{}\".", newCompilation.getTitle());
        List<Event> events = eventUtils.getEventByIds(newCompilation.getEvents());
        Compilation compilation = compilationRepository.save(CompilationMapper.INSTANT.toCompilation(newCompilation, events));
        log.debug("Администратор создал новую подборку \"{}\" с ID = {}.", compilation.getTitle(), compilation.getId());
        return CompilationMapper.INSTANT.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilationById(Long compId, UpdateCompilationRequest updatedCompilation) {
        log.info("Администратор обновляет подборку с ID = {}.", compId);
        Compilation compilationOld = compilationUtils.getCompilationById(compId);
        List<Event> events = eventUtils.getEventByIds(updatedCompilation.getEvents());
        Optional.ofNullable(updatedCompilation.getPinned()).ifPresent(compilationOld::setPinned);
        Optional.ofNullable(updatedCompilation.getTitle()).ifPresent(compilationOld::setTitle);
        if (events != null) {
            compilationOld.setEvents(events);
        }
        Compilation compilation = compilationRepository.save(CompilationMapper.INSTANT.toCompilation(compilationOld, events));
        log.debug("Администратор обновил подборку \"{}\" с ID = {}.", compilation.getTitle(), compilation.getId());
            return CompilationMapper.INSTANT.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void deleteComplication(Long compId) {
        log.debug("Администратор удаляет подборку с ID = {}.", compId);
        compilationRepository.deleteById(compId);
    }

}