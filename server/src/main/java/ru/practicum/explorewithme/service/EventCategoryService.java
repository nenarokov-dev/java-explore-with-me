package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.model.EventCategory;
import ru.practicum.explorewithme.repository.CategoriesRepository;

@Service
@Slf4j
@AllArgsConstructor
public class EventCategoryService {

    private CategoriesRepository categoriesRepository;

    public EventCategory add(EventCategory eventCategory) {
        EventCategory category = categoriesRepository.save(eventCategory);
        log.info("Создана новая категория событий '{}', id={}", category.getName(), category.getId());
        return category;
    }

    public EventCategory patch(EventCategory eventCategory) {
        Long id = eventCategory.getId();
        EventCategory category = categoriesRepository.getReferenceById(id);
        category.setName(eventCategory.getName());
        log.info("Категория событий id={} успешно обновлена.", category.getId());
        return category;
    }

    public void delete(Long id){
        categoriesRepository.deleteById(id);
        log.info("Категория событий id={} успешно удолена.", id);
    }


}
