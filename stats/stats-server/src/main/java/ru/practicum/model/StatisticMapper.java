package ru.practicum.model;

import ru.practicum.dto.StatisticGetDto;
import ru.practicum.dto.StatisticPostDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum StatisticMapper {
    INSTANT;

    public StatisticPostDto toStatisticPostDto (Statistic statistic) {
        StatisticPostDto statisticPostDto = new StatisticPostDto(
                statistic.getIp(),
                statistic.getUri(),
                statistic.getIp(),
                statistic.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return statisticPostDto;
    }

    public Statistic toStatistic (StatisticPostDto statisticPostDto) {
        Statistic statistic = new Statistic();
        statistic.setApp(statisticPostDto.getApp());
        statistic.setIp(statisticPostDto.getIp());
        statistic.setUri(statisticPostDto.getUri());
        statistic.setTimestamp(LocalDateTime.parse(statisticPostDto.getTimestamp(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return statistic;
    }
}
