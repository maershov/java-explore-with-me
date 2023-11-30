package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.GetStatsDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

public interface EndpointHitService {

    EndpointHitDto saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getViewStats(GetStatsDto getStatsDto);
}
