package ru.practicum.event.model;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.LOCAL_VARIABLE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CheckDateValidator.class)
public @interface StartAfterTwoHoursFromNow {
    String message() default "Start can not be earlier than two hours after current time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}