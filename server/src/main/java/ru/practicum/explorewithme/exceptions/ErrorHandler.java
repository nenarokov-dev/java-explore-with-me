package ru.practicum.explorewithme.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.exceptions.models.ApiError;
import ru.practicum.explorewithme.exceptions.models.ErrorMessage;
import ru.practicum.explorewithme.exceptions.models.ErrorResponse;
import ru.practicum.explorewithme.exceptions.models.Violation;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onConstraintValidationException(ConstraintViolationException e) {
        StringBuilder reasons = new StringBuilder();
        for (ConstraintViolation<?> fieldError:e.getConstraintViolations()) {
            reasons.append("Поле: ").append(fieldError.getPropertyPath()).append("\n")
                    .append("Реакция: ").append(fieldError.getMessage());
        }
        return ApiError.builder()
                .message(e.getMessage())
                .reason(reasons.toString())
                .status(HttpStatus.BAD_REQUEST.toString())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder reasons = new StringBuilder();
        for (FieldError fieldError:e.getBindingResult().getFieldErrors()) {
            reasons.append("Поле: ").append(fieldError).append("\n")
                    .append("Реакция: ").append(fieldError.getDefaultMessage());
        }
        return ApiError.builder()
                .message(e.getMessage())
                .reason(reasons.toString())
                .status(HttpStatus.BAD_REQUEST.toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handle(final NotFoundException e) {
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Не удалось найти объект в базе данных.")
                .status(HttpStatus.NOT_FOUND.toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final BadRequestException e) {
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Не удалось найти параметр запроса.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handle(final ForbiddenException e) {
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Не соблюдены условия выполнения операции.")
                .status(HttpStatus.FORBIDDEN.toString())
                .build();
    }

}

