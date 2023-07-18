package ru.practicum.validation;

import org.springframework.stereotype.Component;
import ru.practicum.dto.StatisticPostDto;
import ru.practicum.exception.BadRequest;

import java.util.regex.Pattern;


@Component("statisticValidation")
public class StatisticValidation {

    private Pattern ipPattern = Pattern.compile(
            "((0|1\\d{0,2}|2([0-4][0-9]|5[0-5]))\\.){3}(0|1\\d{0,2}|2([0-4][0-9]|5[0-5]))");

    private Pattern datePattern = Pattern.compile("^(20[23][0-9])-([0][1-9]|[1][012])-([12][0-9]|[3][01]|[0][1-9]) " +
            "([0][1-9]|[1][0-9]|[2][0-3]|[0][0])[:]([0-5][0-9])[:]([0-5][0-9])$");

    public boolean ipIsValid(String ip) {
        if (!ipPattern.matcher(ip).matches()) {
            throw new BadRequest("Невалидный IP адрес.");
        }
        return true;
    }

    public boolean dateIsValid(String date) {
        if (!datePattern.matcher(date).matches()) {
            throw new BadRequest("Невалидная дата. Формат: yyyy-MM-dd HH:mm:ss");
        }
        return true;
    }

    public void statisticDtoIsValid(StatisticPostDto statisticPostDto) {
        ipIsValid(statisticPostDto.getIp());
        dateIsValid(statisticPostDto.getTimestamp());
    }
}