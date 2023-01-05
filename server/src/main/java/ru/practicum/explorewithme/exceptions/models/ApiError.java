package ru.practicum.explorewithme.exceptions.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.component.DateTimeAdapter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ApiError {

    private List<Error> errors;
    private String message;
    private String reason;
    private String status;
    @Builder.Default
    private String timestamp= LocalDateTime.now().format(DateTimeAdapter.formatter);
}
