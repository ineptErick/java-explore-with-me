package ru.practicum.users.dto;

import javax.validation.constraints.NotBlank;

//Пользователь (краткая информация)
public class UserShortDto {
    //Идентификатор
    @NotBlank
    private Long id;
    //Имя
    @NotBlank
    private String name;
}