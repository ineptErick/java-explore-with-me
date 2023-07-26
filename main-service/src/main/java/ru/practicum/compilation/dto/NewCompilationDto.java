package ru.practicum.compilation.dto;

import java.util.List;

//Подборка событий
public class NewCompilationDto {
    //Список идентификаторов событий входящих в подборку
    private List<Long> events;
    //Закреплена ли подборка на главной странице сайта
    //default: false
    private Boolean pinned;
    //Заголовок подборки
    private String title;
}
