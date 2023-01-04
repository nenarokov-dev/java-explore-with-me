package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.model.event.category.EventCategory;
import ru.practicum.explorewithme.pagination.Pagination;
import ru.practicum.explorewithme.repository.CategoriesRepository;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EventCategoryService {

    private CategoriesRepository categoriesRepository;
    private final Pagination<EventCategory> pagination;

    public EventCategory add(EventCategory eventCategory) {
        EventCategory category = categoriesRepository.save(eventCategory);
        log.info("Создана новая категория событий '{}', id={}", category.getName(), category.getId());
        return category;
    }

    public EventCategory patch(EventCategory eventCategory) {
        Long id = eventCategory.getId();
        isCategoryExist(id);
        EventCategory category = categoriesRepository.getReferenceById(id);
        category.setName(eventCategory.getName());
        log.info("Категория событий id={} успешно обновлена.", category.getId());
        return category;
    }

    public List<EventCategory> getAll(Integer from, Integer size) {
        List<EventCategory> categories = categoriesRepository.findAll();
        log.info("Список категорий событий успешно получен.");
        return pagination.setPagination(from, size, categories);
    }

    public EventCategory getById(Long id) {
        isCategoryExist(id);
        EventCategory category = categoriesRepository.getReferenceById(id);
        log.info("Категорий событий id={} успешно получена.", id);
        return category;
    }

    public void delete(Long id) {
        categoriesRepository.deleteById(id);
        log.info("Категория событий id={} успешно удолена.", id);
    }

    private void isCategoryExist(Long id) {
        if (categoriesRepository.findById(id).isEmpty()) {
            log.warn("Категория событий с id={} не найдена.", id);
            throw new NotFoundException("Категория событий с id=" + id + " не найдена.");
        }
    }

}
