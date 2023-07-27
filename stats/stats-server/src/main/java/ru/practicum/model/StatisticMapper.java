package ru.practicum.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.StatisticPostDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public final class StatisticMapper {

    public static StatisticPostDto toStatisticPostDto(Statistic statistic) {
        StatisticPostDto statisticPostDto = new StatisticPostDto(
                statistic.getIp(),
                statistic.getUri(),
                statistic.getIp(),
                statistic.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return statisticPostDto;
    }

    public static Statistic toStatistic(StatisticPostDto statisticPostDto) {
        Statistic statistic = new Statistic();
        statistic.setApp(statisticPostDto.getApp());
        statistic.setIp(statisticPostDto.getIp());
        statistic.setUri(statisticPostDto.getUri());
        statistic.setTimestamp(LocalDateTime.parse(statisticPostDto.getTimestamp(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return statistic;
    }
}
