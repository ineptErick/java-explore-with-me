package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventPrivateController {

    private final EventService eventService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(
            @Positive @PathVariable Long userId,
            @Valid @RequestBody NewEventDto newEvent) {
        return eventService.createEvent(userId, newEvent);
    }

    /*
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(
            @Valid @RequestBody NewCategoryDto newCategory) {
        return categoryService.createCategory(newCategory);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto patchCategoryById(
            @Valid @RequestBody NewCategoryDto updatedCategory,
            @Positive @PathVariable Long catId) {
        return categoryService.patchCategoryById(catId, updatedCategory);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @Positive @PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAllCategories(
            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getAllCategories(
            @Positive @PathVariable Long catId) {
        return categoryService.getCategoryById(catId);
    }
     */

}
