package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.model.ComplexErrorResponse;
import ru.practicum.model.ErrorResponse;
import ru.practicum.model.NotAllowedActionException;
import ru.practicum.model.Violation;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.Constants.*;

@Slf4j
@RestControllerAdvice("ru.practicum")
public class ErrorHandler {

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ComplexErrorResponse onConstraintValidationException(Exception e) {
        ComplexErrorResponse errorResponse = new ComplexErrorResponse(BAD_REQUEST, INCORR_MADE_REQ, e.getMessage(),
                LocalDateTime.now().format(FORMATTER));

        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exp = (ConstraintViolationException) e;
            final List<Violation> violations = exp.getConstraintViolations().stream()
                    .map(violation -> new Violation(
                            violation.getPropertyPath().toString(),
                            violation.getMessage()))
                    .collect(Collectors.toList());
            log.debug("Получен статус 400 Bad request; violations: {}", violations, e);

            errorResponse.setErrors(violations);
            return errorResponse;
        } else {
            MethodArgumentNotValidException exp = (MethodArgumentNotValidException) e;
            final List<Violation> violations = exp.getBindingResult().getFieldErrors().stream()
                    .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());
            log.debug("Получен статус 400 Bad; violations: {}", violations, e);
            errorResponse.setErrors(violations);
            return errorResponse;
        }
    }

    @ExceptionHandler({
            NotAllowedActionException.class,
            IllegalArgumentException.class,
            org.springframework.web.bind.MissingServletRequestParameterException.class,
            ConversionFailedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onNotAllowedActionException(RuntimeException e) {
        log.debug("Получен статус 400 Bad request; message:{}", e.getMessage(), e);
        return new ErrorResponse(BAD_REQUEST, INCORR_MADE_REQ, e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse onConstraintValidationException(ru.practicum.model.ConstraintViolationException e) {
        log.debug("Получен статус 409 CONFLICT; message:{}", e.getMessage(), e);
        return new ErrorResponse(FORBIDDEN, "Incorrectly made request", e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse onUnexpectedError(Throwable e) {
        log.debug("Получен статус 500 Internal server error {}", e.getMessage(), e);
        return new ErrorResponse(INTERNAL_SERVER_ERROR, Arrays.toString(e.getStackTrace()), e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }
}
