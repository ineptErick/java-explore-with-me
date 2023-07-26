package ru.practicum.compilation.service.publicPart;


import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationPubService {

    CompilationDto getCompilationByIdPublic(Long compId);

    List<CompilationDto> getComplicationsPublic(Boolean pinned, Integer from, Integer size);

}