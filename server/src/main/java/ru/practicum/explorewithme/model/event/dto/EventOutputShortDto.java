package ru.practicum.explorewithme.model.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.model.category.dto.CategoryDto;
import ru.practicum.explorewithme.model.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@Builder
public class EventOutputShortDto {

    private Long id;
    @NotBlank(message = "Заголовок события не должен быть пустым.")
    private String title;
    @NotBlank(message = "Аннотация события не должна быть пустой.")
    private String annotation;
    @NotNull(message = "Событие должно относиться к какой-то категории.")
    private CategoryDto category;
    @NotNull(message = "Дата события должна быть задана.")
    private String eventDate;
    @NotNull(message = "У события должен быть инициатор.")
    private UserShortDto initiator;
    private boolean paid;
    @PositiveOrZero(message = "Число просмотров не может быть меньше нуля.")
    private Long views;
    @PositiveOrZero(message = "Количество подтвержденных запросов на участие в событии не может быть меньше нуля.")
    private Long confirmedRequests;
}
