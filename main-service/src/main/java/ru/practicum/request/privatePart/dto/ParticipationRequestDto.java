package ru.practicum.request.privatePart.dto;
//Заявка на участие в событии
public class ParticipationRequestDto {
    //Дата и время создания заявки
    private String created;
    //Идентификатор события
    private Long event;
    //Идентификатор заявки
    private Long id;
    //Идентификатор пользователя, отправившего заявку
    private Long requester;
    //Статус заявки
    private String status;
}