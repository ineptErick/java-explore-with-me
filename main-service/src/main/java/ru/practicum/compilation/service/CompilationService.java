package ru.practicum.compilation.service;


import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilationDto newCompilation);

    CompilationDto updateCompilationById(Long compId, UpdateCompilationRequest updatedCompilation);

    Compilation getCompilationById(Long compId);

    void deleteComplication(Long compId);

    CompilationDto getCompilationByIdPublic(Long compId);

    List<CompilationDto> getComplicationsPublic(Boolean pinned, Integer from, Integer size);
}
