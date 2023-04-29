package ru.practicum.explorewithme.model.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class EventCompilationDto {

    private Long id;
    private Set<Long> events;
    @Builder.Default
    private Boolean pinned = false;
    @NotBlank(message = "Название подборки не должно быть пустым.")
    private String title;
}
