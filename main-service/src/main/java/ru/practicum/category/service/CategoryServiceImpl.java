package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ApiError.exception.BadRequestException;
import ru.practicum.ApiError.exception.ConflictException;
import ru.practicum.ApiError.exception.NotFoundException;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategory) {
        log.info("Создание новой категории: {}.", newCategory.getName());
        isCategoryNameIsBusy(newCategory.getName());
        Category category = categoryRepository.save(
                CategoryMapper.INSTANT.newCategoryDtoToCategory(newCategory));
        log.debug("Категория создана. ID = {}.", category.getId());
        return CategoryMapper.INSTANT.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto patchCategoryById(Long catId, NewCategoryDto updatedCategory) {
        log.info("Обновление категории с ID = {}.", catId);
        isCategoryNameIsBusy(updatedCategory.getName());
        isCategoryPresent(catId);
        Category category = categoryRepository.getCategoryById(catId);
        category.setName(updatedCategory.getName());
        categoryRepository.save(category);
        log.debug("Категория с ID = {} обновлена.", catId);
        return CategoryMapper.INSTANT.toCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        log.info("Удаление категории с ID = {}.", catId);
        isCategoryPresent(catId);
        isCategoryUsing(catId);
        categoryRepository.deleteById(catId);
        log.debug("Категории с ID = {} удалена.", catId);
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        Integer page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        log.info("Выгрузка списка категорий с параметрами: size={}, from={}.", size, page);
        Page<Category> pageCategory = categoryRepository.getAllCategoriesById(pageRequest);
        List<Category> requests = pageCategory.getContent();
        List<CategoryDto> requestsDto = requests.stream()
                    .map(request -> CategoryMapper.INSTANT.toCategoryDto(request))
                    .collect(Collectors.toList());
        return requestsDto;
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        log.info("Получение категории по ID = {}.", catId);
        return CategoryMapper.INSTANT.toCategoryDto(categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Категория с ID = " + catId + " не найдена.")
        ));
    }

    @Override
    public Category getCategoryModelById(Long catId) {
        log.info("Получение категории по ID = {}.", catId);
        return categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Категория с ID = " + catId + " не найдена."));
    }

    @Override
    public void isCategoryNameIsBusy(String name) {
        if (categoryRepository.findFirstByName(name) != null) {
            log.error("Категория \"{}\" уже существует.",name);
            throw new ConflictException("Категория уже существует.");
        }
    }

    @Override
    public void isCategoryPresent(Long catId) {
        if (categoryRepository.getCategoryById(catId) == null) {
            log.error("Категория c ID = {} не существует.",catId);
            throw new BadRequestException("Категория c ID = " + catId + " не существует.");
        }
    }

    @Override
    public void isCategoryUsing(Long catId) {
        if (eventRepository.findFirstByCategory(catId) != null) {
            log.error("Категория c ID = {} используется и не может быть удалена.",catId);
            throw new ConflictException("Категория c ID = " + catId + " используется и не может быть удалена.");
        }
    }
}
