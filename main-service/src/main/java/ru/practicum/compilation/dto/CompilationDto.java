package ru.practicum.compilation.dto;

import ru.practicum.event.dto.EventShortDto;

import java.util.List;

//Подборка событий
public class CompilationDto {

    //Список событий входящих в подборку
    private List<EventShortDto> events;
    //Идентификатор
    private Long id;
    //Закреплена ли подборка на главной странице сайта
    private Boolean pinned;
    //Заголовок подборки
    private String title;
}