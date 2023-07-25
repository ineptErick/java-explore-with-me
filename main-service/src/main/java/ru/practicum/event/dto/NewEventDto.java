package ru.practicum.event.dto;

import ru.practicum.location.Location;

//Новое событие
public class NewEventDto {
    //Краткое описание события
    //maxLength: 2000
    //minLength: 20
    private String annotation;
    //id категории к которой относится событие
    private Long category;
    //Полное описание события
    //maxLength: 7000
    //minLength: 20
    private String description;
    //Дата и время на которые намечено событие в формате "yyyy-MM-dd HH:mm:ss"
    private String eventDate;
    //Широта и долгота места проведения события
    private Location location;
    //Нужно ли оплачивать участие в событии
    //default: false
    private Boolean paid;
    //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    //default: 0
    private Integer participantLimit;
    //Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события.
    //Если false - то будут подтверждаться автоматически.
    //default: true
    private Boolean requestModeration;
    //Заголовок события
    //maxLength: 120
    //minLength: 3
    private String title;
}