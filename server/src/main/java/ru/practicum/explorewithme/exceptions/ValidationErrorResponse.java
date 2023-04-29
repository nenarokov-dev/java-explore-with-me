package ru.practicum.explorewithme.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.explorewithme.exceptions.models.Violation;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {

    private final List<Violation> violations;
}
