package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.model.ErrorResponse;
import ru.practicum.model.ValidationErrorResponse;
import ru.practicum.model.Violation;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice("ru.practicum")
public class ErrorHandler {
    @ExceptionHandler({javax.validation.ConstraintViolationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(RuntimeException e) {
        if (e instanceof javax.validation.ConstraintViolationException) {
            javax.validation.ConstraintViolationException exp = (javax.validation.ConstraintViolationException) e;
            final List<Violation> violations = exp.getConstraintViolations().stream()
                    .map(violation -> new Violation(
                            violation.getPropertyPath().toString(),
                            violation.getMessage()))
                    .collect(Collectors.toList());
            log.debug("Получен статус 400 Bad request (ConstraintValidationException); violations: {}", violations);
            return new ValidationErrorResponse(violations);
        }
        return new ValidationErrorResponse(List.of());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onIllegalArgumentException(RuntimeException e) {
        if (e instanceof IllegalArgumentException) {
            log.debug("Получен статус 400 Bad request (IllegalArgumentException): {}", e.getMessage());
            return new ErrorResponse(e.getMessage(), "");
        }
        log.debug("Получен статус 400 Bad request (IllegalArgumentException): {}", e.getMessage());
        return new ErrorResponse(e.toString(), "возникла непредвиденная ошибка ограничения " +
                "переданного параметра");
    }

    @ExceptionHandler({ru.practicum.ConstraintViolationException.class, DateTimeParseException.class,
            org.springframework.web.bind.MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onConstraintValidationException(Exception e) {
        if (e instanceof ru.practicum.ConstraintViolationException) {
            ru.practicum.ConstraintViolationException exp = (ru.practicum.ConstraintViolationException) e;
            log.debug("Получен статус 400 Conflict; value:{}", exp.getValue(), e);
            return new ErrorResponse(String.format("Значение ошибки - (%s)", exp.getValue()), exp.getMessage());
        } else if (e instanceof org.springframework.web.bind.MissingServletRequestParameterException) {
            log.debug("Получен статус 400 Bad request " +
                    "(org.springframework.web.bind.MissingServletRequestParameterException): {}", e.getMessage());
            return new ErrorResponse(e.getMessage(), "");
        } else if (e instanceof DateTimeParseException) {
            DateTimeParseException exp = (DateTimeParseException) e;
            log.debug("Получен статус 400 Conflict; value:{}", exp.getMessage(), e);
            return new ErrorResponse(exp.getMessage(), exp.getMessage());
        }
        return new ErrorResponse(e.toString(), "возникла непредвиденная ошибка ограничения " +
                "передаваемых данных");
    }

    //@ExceptionHandler
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    //public ErrorResponse onUnexpectedError(Throwable e) {
    //    log.debug("Получен статус 500 Internal server error {}", e.getMessage(), e);
    //    return new ErrorResponse(e.toString(), "возникла непредвиденная ошибка");
    //}
}
