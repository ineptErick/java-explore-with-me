package ru.practicum.service;

import ru.practicum.dto.StatisticGetDto;
import ru.practicum.dto.StatisticPostDto;

import java.util.List;

public interface StatisticService {
    List<StatisticGetDto> getStatistic(String start, String end, String[] uris, Boolean isUnique);

    StatisticPostDto addStatistic(StatisticPostDto statisticPostDto);
}
