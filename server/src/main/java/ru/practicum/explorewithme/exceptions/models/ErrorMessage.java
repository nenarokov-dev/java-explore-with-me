package ru.practicum.explorewithme.exceptions.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorMessage {

    private final String error;
}
