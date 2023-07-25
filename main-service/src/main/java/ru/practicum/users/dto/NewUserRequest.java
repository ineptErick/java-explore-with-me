package ru.practicum.users.dto;

import javax.validation.constraints.NotBlank;

//Данные нового пользователя
public class NewUserRequest {
    //Почтовый адрес
    @NotBlank
    private String email;
    //Имя
    @NotBlank
    private String name;
}