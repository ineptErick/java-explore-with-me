package ru.practicum.event.dto;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.location.LocationDto;
import ru.practicum.users.dto.UserShortDto;

public class EventFullDto {
    //Краткое описание
    private String annotation;
    //Категория
    private CategoryDto category;
    //Количество одобренных заявок на участие в данном событии
    private Integer confirmedRequests;
    //Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    private String createdOn;
    //Полное описание события
    private String description;
    //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    //Идентификатор
    private Long id;
    //Пользователь (краткая информация)
    private UserShortDto initiator;
    //Широта и долгота места проведения события
    private LocationDto location;
    //Нужно ли оплачивать участие
    private Boolean paid;
    //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    //default: 0
    private Integer participantLimit;
    //Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    private String publishedOn;
    //Нужна ли пре-модерация заявок на участие
    //default: true
    private Boolean requestModeration;
    //Список состояний жизненного цикла события
    //PENDING, PUBLISHED, CANCELED
    private String state;
    //Заголовок
    private String title;
    //Количество просмотрев события
    private Long views;


}
