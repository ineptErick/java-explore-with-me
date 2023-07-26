package ru.practicum.category.dto;

import javax.validation.constraints.NotBlank;

//Категория
public class CategoryDto {

    //Идентификатор категории
    //readOnly: true
    @NotBlank
    private Long id;
    //Название категории
    @NotBlank
    private String name;
}
