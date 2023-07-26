package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatisticPostDto;
import ru.practicum.exception.BadRequest;
import ru.practicum.validation.StatisticValidation;

import java.util.Map;
import java.util.Set;


@RequiredArgsConstructor
@Slf4j
@Validated
@RestController

public class StatisticClientController {

    private final StatisticClient statisticClient;

    private final StatisticValidation validation;


    @PostMapping("/hit")
    public ResponseEntity<Object> addHit(
            @RequestBody StatisticPostDto statisticPostDto) {
        validation.statisticDtoIsValid(statisticPostDto);
        log.info("Добавлен просмотр в статистику для URI: {}", statisticPostDto.getUri());
        return statisticClient.post("/hit",statisticPostDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStatistic(
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
        return statisticClient.get("/stats", params, uris);
    }

}