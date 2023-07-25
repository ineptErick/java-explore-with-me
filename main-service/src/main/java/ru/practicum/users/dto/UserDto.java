package ru.practicum.users.dto;

import javax.validation.constraints.NotBlank;

//TODO
// Валидацию почты через регулярные выражения
//Имя NotBlank
//Пользователь
public class UserDto {
    //Почтовый адрес
    @NotBlank
    private String email;
    //Идентификатор
    //readOnly: true
    @NotBlank
    private Long id;
    //Имя
    @NotBlank
    private String name;
}