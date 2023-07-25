package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatisticGetDto;
import ru.practicum.dto.StatisticPostDto;
import ru.practicum.service.StatisticService;
import ru.practicum.validation.StatisticValidation;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatisticController {

    private final StatisticService statisticService;

    private final StatisticValidation validation;

    @PostMapping("/hit")
    public StatisticPostDto addHit(
            @RequestBody StatisticPostDto statisticPostDto) {
        validation.statisticDtoIsValid(statisticPostDto);
        log.info("Добавлен просмотр в статистику для URI: {}", statisticPostDto.getUri());
        return statisticService.addStatistic(statisticPostDto);
    }

    @GetMapping("/stats")
    public List<StatisticGetDto> getStatistic(
            @RequestParam(value = "start", required = true) String start,
            @RequestParam(value = "end", required = true) String end,
            @RequestParam(value = "uris", required = false) String[] uris,
            @RequestParam(value = "unique", defaultValue = "false") Boolean isUnique) {
        validation.dateIsValid(start);
        validation.dateIsValid(end);
        log.info("Запрос статистики с параметрами: \n start={} \n end={} \n isUnique={} \n uris={}", start, end, isUnique, uris);
        return statisticService.getStatistic(start, end, uris, isUnique);
    }

}
