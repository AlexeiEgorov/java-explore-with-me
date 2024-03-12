package ru.practicum.event.model;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

@Component
public class CheckDateValidator implements ConstraintValidator<StartAfterTwoHoursFromNow, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime start, ConstraintValidatorContext constraintValidatorContext) {
        if (start == null) {
            return true;
        }
        return start.isAfter(LocalDateTime.now().plusHours(2));
    }
}