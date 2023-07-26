package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.enums.EventStateAction;
import ru.practicum.event.model.Event;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto newCategory);

    CategoryDto patchCategoryById(Long catId, NewCategoryDto updateCategory);

    void deleteCategory(Long catId);

    List<CategoryDto> getAllCategories(Integer from, Integer size);


    CategoryDto getCategoryById(Long catId);

    Category getCategoryModelById(Long catId);


    void isCategoryNameIsBusy(String name);

    void isCategoryPresent(Long catId);

    void isCategoryUsing(Long catId);


}
