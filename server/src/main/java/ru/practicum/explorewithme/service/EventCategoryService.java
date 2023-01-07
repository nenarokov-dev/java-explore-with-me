package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.component.BeanFinder;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.exceptions.DuplicateException;
import ru.practicum.explorewithme.model.category.EventCategory;
import ru.practicum.explorewithme.model.category.dto.CategoryDto;
import ru.practicum.explorewithme.model.category.mapper.CategoriesMapper;
import ru.practicum.explorewithme.pagination.Pagination;
import ru.practicum.explorewithme.repository.CategoriesRepository;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EventCategoryService {

    private CategoriesRepository categoriesRepository;
    private final Pagination<EventCategory> pagination;

    public EventCategory add(CategoryDto eventCategory) {
        EventCategory category = categoriesRepository.save(CategoriesMapper.toCategoryFromDto(eventCategory));
        log.info("Создана новая категория событий '{}', id={}", category.getName(), category.getId());
        return category;
    }

    public CategoryDto patch(CategoryDto eventCategory) {
        Long id = eventCategory.getId();
        if (id == null) {
            String message = "Идентификатор категории должен быть передан в теле запроса.";
            log.warn(message);
            throw new BadRequestException(message);
        }
        EventCategory category = categoriesRepository.getReferenceById(id);
        if (categoriesRepository.findByName(eventCategory.getName()) != null) {
            String message = "Нарушение уникальности категории события.";
            log.warn(message);
            throw new DuplicateException(message);
        }
        category.setName(eventCategory.getName());
        log.info("Категория событий id={} успешно обновлена.", category.getId());
        return CategoriesMapper.toCategoryDto(category);
    }

    public List<EventCategory> getAll(Integer from, Integer size) {
        List<EventCategory> categories = categoriesRepository.findAll();
        log.info("Список категорий событий успешно получен.");
        return pagination.setPagination(from, size, categories);
    }

    public EventCategory getById(Long id) {
        BeanFinder.findEventCategoryById(id, categoriesRepository);
        EventCategory category = categoriesRepository.getReferenceById(id);
        log.info("Категорий событий id={} успешно получена.", id);
        return category;
    }

    public void delete(Long id) {
        categoriesRepository.deleteById(id);
        log.info("Категория событий id={} успешно удолена.", id);
    }

}
