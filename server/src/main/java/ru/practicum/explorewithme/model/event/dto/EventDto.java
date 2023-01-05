package ru.practicum.explorewithme.model.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.explorewithme.annotation.IsTimeLaterThenValue;
import ru.practicum.explorewithme.model.location.dto.LocationDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class EventDto {

    private Long eventId;
    @Length(max = 120, min = 3,
            message = "Заголовок события не должен быть короче 3 символов, и не должен быть длиннее 120 символов.")
    private String title;
    @Length(max = 2000, min = 20,
            message = "Краткое описание события не должно быть короче 20 символов," +
                    " и не должен быть длиннее 2000 символов.")
    private String annotation;
    @Length(max = 7000, min = 20,
            message = "Описание события не должно быть короче 20 символов, и не должно быть длиннее 7000 символов.")
    private String description;
    @NotNull(message = "Событие должно относиться к какой-то категории.")
    @Positive(message = "Идентификатор категории должен быть больше нуля.")
    private Long category;
    @IsTimeLaterThenValue(hours = 2, message = "Начало события должно быть не менее чем через 2 часа.")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    @Builder.Default
    private Boolean requestModeration = true;
}
