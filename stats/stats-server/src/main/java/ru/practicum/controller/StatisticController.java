package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatisticGetProjection;
import ru.practicum.dto.StatisticPostDto;
import ru.practicum.exception.BadRequest;
import ru.practicum.service.StatisticService;
import ru.practicum.validation.StatisticValidation;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public List<StatisticGetProjection> getStatistic(
            @RequestParam Map<String, String> params,
            @RequestParam(value = "uris", required = false) Set<String> uris) {
        if (!params.containsKey("start") || !params.containsKey("end")) {
            log.info("Не заданы обязательные параметры: start и/или end.");
            throw new BadRequest("Не заданы обязательные параметры: start и/или end.");
        }
        validation.dateIsValid(params.get("start"));
        validation.dateIsValid(params.get("end"));
        log.info("Запрос статистики с параметрами: \n start={} \n end={} \n isUnique={} \n uris={}",
                params.get("start"), params.get("end"), params.get("unique"), params.get("uris"));
        return statisticService.getStatistic("/stats", params, uris);
    }
}
