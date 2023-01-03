package ru.practicum.explorewithme.model.event.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;
}
