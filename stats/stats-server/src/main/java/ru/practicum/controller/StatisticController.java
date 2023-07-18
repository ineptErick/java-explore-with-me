package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatisticGetDto;
import ru.practicum.dto.StatisticPostDto;
import ru.practicum.service.StatisticService;
import ru.practicum.validation.StatisticValidation;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    private final StatisticValidation validation;

    @PostMapping("/hit")
    public StatisticPostDto addHit(
            @RequestBody StatisticPostDto statisticPostDto) {
        validation.statisticDtoIsValid(statisticPostDto);
        return statisticService.addStatistic(statisticPostDto);
    }

    @GetMapping("/stats")
    public List<StatisticGetDto> getStatistic(
            @RequestParam(value = "start", required = true) String start,
            @RequestParam(value = "end", required = true) String end,
            @RequestParam(value = "uris", required = false) String[] uris,
            @RequestParam(value = "isUnique", defaultValue = "false") Boolean isUnique) {
        validation.dateIsValid(start);
        validation.dateIsValid(end);
        return statisticService.getStatistic(start, end, uris, isUnique);
    }

}
