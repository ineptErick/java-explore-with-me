package ru.practicum.event.dto;

import ru.practicum.location.LocationDto;

//Данные для изменения информации о событии. Если поле в запросе не указано (равно null) -
//значит изменение этих данных не требуется.
public class UpdateEventUserRequest {
    //Новая аннотация
    //maxLength: 2000
    //minLength: 20
    private String annotation;
    //id новой категории
    private Long category;
    //Новое описание
    //maxLength: 7000
    //minLength: 20
    private String description;
    //Новые дата и время на которые намечено событие в формате "yyyy-MM-dd HH:mm:ss"
    private String eventDate;
    //Широта и долгота места проведения события
    private LocationDto location;
    //Новое значение флага о платности мероприятия
    private Boolean paid;
    //Новый лимит пользователей
    private Integer participantLimit;
    //Нужна ли пре-модерация заявок на участие
    private Boolean requestModeration;
    //Новое состояние события
    private String stateAction;
    //Новый заголовок
    //maxLength: 120
    //minLength: 3
    private String title;
}
