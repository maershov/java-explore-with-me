package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto createNewCompilation(NewCompilationDto newCompilationDto);

    void removeCompilation(Long id);

    CompilationDto getCompilationById(Long id);

    List<CompilationDto> getCompilationsByPinned(Boolean pinned, Integer from, Integer size);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
