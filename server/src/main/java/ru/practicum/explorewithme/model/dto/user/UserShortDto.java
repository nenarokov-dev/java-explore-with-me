package ru.practicum.explorewithme.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
public class UserShortDto {

    private Long id;
    @NotBlank(message = "Имя пользователя не должно быть пустым.")
    private String name;

}
