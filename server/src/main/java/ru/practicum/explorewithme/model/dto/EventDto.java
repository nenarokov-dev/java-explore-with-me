package ru.practicum.explorewithme.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.explorewithme.annotation.IsTimeLaterThenValue;
import ru.practicum.explorewithme.model.Location;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class EventDto {

    @Length(max = 120,min = 3,
            message = "Заголовок события не должен быть короче 3 символов, и не должен быть длиннее 120 символов.")
    private String title;
    @Length(max = 2000,min = 20,
            message = "Краткое описание события не должно быть короче 20 символов," +
                    " и не должен быть длиннее 2000 символов.")
    private String annotation;
    @Length(max = 7000,min = 20,
            message = "Описание события не должно быть короче 20 символов, и не должно быть длиннее 7000 символов.")
    private String description;
    @Future
    private int category;
    @IsTimeLaterThenValue(hours = 2,message = "Начало события должно быть не менее чем через 2 часа.")
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    @Builder.Default
    private Boolean requestModeration = true;
}
