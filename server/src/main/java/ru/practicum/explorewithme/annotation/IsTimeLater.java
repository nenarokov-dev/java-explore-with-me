package ru.practicum.explorewithme.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;
import java.time.DateTimeException;
import java.time.LocalDateTime;

public class IsTimeLater implements ConstraintValidator<IsTimeLaterThenValue, LocalDateTime> {
    int hours;

    @Override
    public void initialize(IsTimeLaterThenValue isTimeLaterThenValue) {
        this.hours = isTimeLaterThenValue.hours();
    }

    @Override
    public boolean isValid(@NotNull LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        } else {
            try {
                return value.isAfter(LocalDateTime.now().plusHours(hours));
            } catch (DateTimeException e) {
                return false;
            }
        }

    }

}
