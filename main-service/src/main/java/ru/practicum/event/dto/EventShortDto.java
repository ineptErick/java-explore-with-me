package ru.practicum.event.dto;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.users.dto.UserShortDto;

//Краткая информация о событии
public class EventShortDto {
    //Краткое описание
    private String annotation;
    //Категория
    private CategoryDto category;
    //Количество одобренных заявок на участие в данном событии
    private Integer confirmedRequests;
    //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    //Идентификатор
    private Long id;
    //Пользователь (краткая информация)
    private UserShortDto initiator;
    //Нужно ли оплачивать участие
    private Boolean paid;
    //Заголовок
    private String title;
    //Количество просмотрев события
    private Long views;
}
