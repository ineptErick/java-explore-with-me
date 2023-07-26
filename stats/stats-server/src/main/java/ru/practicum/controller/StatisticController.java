package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.exception.BadRequest;
import ru.practicum.service.StatisticService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatisticController {

    private final StatisticService statisticService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit addHit(
            @RequestBody EndpointHit endpointHit) {
        log.info("Добавлен просмотр в статистику для URI: {}", endpointHit.getUri());
        return statisticService.save(endpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStats> getStatistic(
            @RequestParam Map<String, String> params,
            @RequestParam(value = "uris", required = false) Set<String> uris) {
        if (!params.containsKey("start") || !params.containsKey("end")) {
            log.info("Не заданы обязательные параметры: start и/или end.");
            throw new BadRequest("Не заданы обязательные параметры: start и/или end.");
        }
        log.info("Запрос статистики с параметрами: \n start={} \n end={} \n isUnique={} \n uris={}",
                params.get("start"), params.get("end"), params.get("unique"), params.get("uris"));
        return statisticService.getStatistic(params, uris);
    }

}