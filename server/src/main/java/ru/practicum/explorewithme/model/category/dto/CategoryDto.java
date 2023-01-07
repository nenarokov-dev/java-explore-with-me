package ru.practicum.explorewithme.model.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    @NotBlank(message = "Наименование категории не должно быть пустым.")
    private String name;
}
