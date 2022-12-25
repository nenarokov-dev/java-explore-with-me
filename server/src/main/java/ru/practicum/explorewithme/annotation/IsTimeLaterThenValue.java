package ru.practicum.explorewithme.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = IsTimeLater.class)
@Documented
public @interface IsTimeLaterThenValue {

    String message() default "{IsTimeLater.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int hours();

}
