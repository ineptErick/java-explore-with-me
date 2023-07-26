package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatisticGetProjection;
import ru.practicum.dto.StatisticPostDto;
import ru.practicum.model.StatisticMapper;
import ru.practicum.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    @Override
    public List<StatisticGetProjection> getStatistic(String path, Map<String, String> params, Set<String> uris) {
        boolean isUnique = params.containsKey("unique") ? Boolean.parseBoolean(params.get("unique")) : false;
        uris = uris == null ? new HashSet<>() : uris;
        if (uris.size() > 0 && !isUnique) {
            return statisticRepository.getUrisViewsFromSet(uris, stringToLocalDate(params.get("start")),
                    stringToLocalDate(params.get("end")));
        } else if (uris.size() > 0 && isUnique) {
            return statisticRepository.getUrisViewsFromSetUnique(uris, stringToLocalDate(params.get("start")),
                    stringToLocalDate(params.get("end")));
        } else if (isUnique) {
            return statisticRepository.getUrisViewsUnique(stringToLocalDate(params.get("start")),
                    stringToLocalDate(params.get("end")));
        } else {
            return statisticRepository.getUrisViews(stringToLocalDate(params.get("start")),
                    stringToLocalDate(params.get("end")));
        }
    }

    @Override
    public StatisticPostDto addStatistic(StatisticPostDto statisticPostDto) {
        statisticRepository.save(StatisticMapper.INSTANT.toStatistic(statisticPostDto));
        return statisticPostDto;
    }

    private LocalDateTime stringToLocalDate(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
