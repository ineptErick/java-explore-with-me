package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.practicum.dto.StatisticGetDto;
import ru.practicum.dto.StatisticPostDto;
import ru.practicum.validation.StatisticValidation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.RequestEntity.post;


@RequiredArgsConstructor
@Slf4j
@Validated
@RestController

public class StatisticClientController {

    private final StatisticClient statisticClient;

    private final StatisticValidation validation;
    private final String serverUrl = "http://localhost:9090";


    @PostMapping("/hit")
    public ResponseEntity<Object> addHit(
            @RequestBody StatisticPostDto statisticPostDto) {
        validation.statisticDtoIsValid(statisticPostDto);
        return statisticClient.post(serverUrl + "/hit",statisticPostDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStatistic (
            @RequestParam(value = "start", required = true) String start,
            @RequestParam(value = "end", required = true) String end,
            @RequestParam(value = "uris", required = false) String[] uris,
            @RequestParam(value = "isUnique", defaultValue = "false") Boolean isUnique) {
        validation.dateIsValid(start);
        validation.dateIsValid(end);
        return statisticClient.get(serverUrl + "/stats", start, end, uris, isUnique);
    }


}
