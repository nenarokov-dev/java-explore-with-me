package ru.practicum.explorewithme.model.event.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EventCompilationDto {

    private Long id;
    @NotBlank(message = "В подборке событий должно быть хотя бы одно событие.")
    private List<Long> events;
    @Builder.Default
    private Boolean pinned = false;
    @NotBlank(message = "Название подборки не должно быть пустым.")
    private String title;
}
