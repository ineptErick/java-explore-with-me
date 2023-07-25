package ru.practicum.compilation.dto;

import java.util.List;

//Изменение информации о подборке событий. Если поле в запросе не указано (равно null) -
//значит изменение этих данных не требуется.
public class UpdateCompilationRequest {
    //Список идентификаторов событий входящих в подборку
    //uniqueItems: true
    private List<Long> events;
    //Закреплена ли подборка на главной странице сайта
    private Boolean pinned;
    //Заголовок подборки
    private String title;
}