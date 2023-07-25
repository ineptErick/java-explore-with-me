package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatisticGetDto;
import ru.practicum.dto.StatisticPostDto;
import ru.practicum.model.StatisticMapper;
import ru.practicum.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    @Override
    public List<StatisticGetDto> getStatistic(String start, String end, String[] uris, Boolean isUnique) {
        List<StatisticGetDto> result = new ArrayList<>();
        if (uris == null && !isUnique) {
            result = statisticRepository.getUrisViews(stringToLocalDate(start), stringToLocalDate(end));
        } else if (uris == null && isUnique) {
            result = statisticRepository.getUrisViewsUnique(stringToLocalDate(start), stringToLocalDate(end));
        } else if (uris != null && !isUnique) {
            for (String uri: uris) {
                result.add(statisticRepository.getUriViews(uri, stringToLocalDate(start), stringToLocalDate(end)));
            }
            result.sort(Comparator.comparing(StatisticGetDto::getHits).reversed());
        } else {
            for (String uri: uris) {
                result.add(statisticRepository.getUriViewsUnique(uri, stringToLocalDate(start), stringToLocalDate(end)));
            }
            result.sort(Comparator.comparing(StatisticGetDto::getHits).reversed());
        }
       return result;
    }

    @Override
    public StatisticPostDto addStatistic(StatisticPostDto statisticPostDto) {
        statisticRepository.save(StatisticMapper.toStatistic(statisticPostDto));
        return statisticPostDto;
    }

    private LocalDateTime stringToLocalDate(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
