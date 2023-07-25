package ru.practicum.event.enums;

public enum State {
    PENDING, //Ожидание публикации: по умолчанию
    PUBLISHED, //Публикация: только администратор
    CANCELED //Отмена публикации: отменил администратор; отменил инициатор на этапе PENDING
}