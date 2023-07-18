package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatisticPostDto;
import ru.practicum.validation.StatisticValidation;



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
            @RequestParam(value = "start", required = true) String start,
            @RequestParam(value = "end", required = true) String end,
            @RequestParam(value = "uris", required = false) String[] uris,
            @RequestParam(value = "isUnique", defaultValue = "false") Boolean isUnique) {
        validation.dateIsValid(start);
        validation.dateIsValid(end);
        log.info("Запрос статистики с параметрами: \n start={} \n end={} \n isUnique={} \n uris={}", start, end, isUnique, uris);
        return statisticClient.get("/stats", start, end, uris, isUnique);
    }

}