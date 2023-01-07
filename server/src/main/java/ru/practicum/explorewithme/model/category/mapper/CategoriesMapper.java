package ru.practicum.explorewithme.model.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.category.EventCategory;
import ru.practicum.explorewithme.model.category.dto.CategoryDto;

@Component
public class CategoriesMapper {

    public static CategoryDto toCategoryDto(EventCategory category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static EventCategory toCategoryFromDto(CategoryDto category) {
        return EventCategory.builder()
                .name(category.getName())
                .build();
    }

}