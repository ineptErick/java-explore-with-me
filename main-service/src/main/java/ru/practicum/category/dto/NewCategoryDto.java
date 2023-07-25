package ru.practicum.category.dto;

import javax.validation.constraints.NotBlank;

//Данные для добавления новой категории
public class NewCategoryDto {
    //Название категории
    @NotBlank
    private String name;
}