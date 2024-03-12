package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.model.ErrorResponse;
import ru.practicum.model.NotAllowedActionException;
import ru.practicum.model.Violation;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.Constants.*;

@Slf4j
@RestControllerAdvice("ru.practicum")
public class ErrorHandler {
    private static final String INCORR_MADE_REQ = "Incorrectly made request";

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onConstraintValidationException(Exception e) {
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exp = (ConstraintViolationException) e;
            final List<Violation> violations = exp.getConstraintViolations().stream()
                    .map(violation -> new Violation(
                            violation.getPropertyPath().toString(),
                            violation.getMessage()))
                    .collect(Collectors.toList());
            log.debug("Получен статус 400 Bad request (ConstraintValidationException); violations: {}", violations);
            ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST, "Incorrectly made request",
                    e.getMessage(),
                    LocalDateTime.now().format(FORMATTER));
            errorResponse.setErrors(violations);
            return errorResponse;
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exp = (MethodArgumentNotValidException) e;
            final List<Violation> violations = exp.getBindingResult().getFieldErrors().stream()
                    .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.toList());
            log.debug("Получен статус 400 Bad request (MethodArgumentNotValidException); violations: {}", violations);
            ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST, "Incorrectly made request",
                    e.getMessage(),
                    LocalDateTime.now().format(FORMATTER));
            errorResponse.setErrors(violations);
            return errorResponse;
        }
        return new ErrorResponse(BAD_REQUEST, "Incorrectly made request", e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse onNotAllowedActionException(NotAllowedActionException e) {
        log.debug("Получен статус 409 Conflict (NotAllowedActionException); value:{}", e.getValue());
        return new ErrorResponse(BAD_REQUEST, INCORR_MADE_REQ, e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse onConstraintValidationException(ru.practicum.model.ConstraintViolationException e) {
        log.debug("Получен статус 409 CONFLICT (ru.practicum.ConstraintViolationException); value:{}", e.getValue());
        return new ErrorResponse(FORBIDDEN, "Incorrectly made request", e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onIllegalArgumentException(IllegalArgumentException e) {
        log.debug("Получен статус 400 Bad request (IllegalArgumentException): {}", e.getMessage());
        return new ErrorResponse(BAD_REQUEST, INCORR_MADE_REQ, e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onMissingServletRequestParameterException(
            org.springframework.web.bind.MissingServletRequestParameterException e) {
        log.debug("Получен статус 400 Bad request (MissingServletRequestParameterException): {}", e.getMessage());
        return new ErrorResponse(BAD_REQUEST, "Incorrectly made request", e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onConversionFailedException(ConversionFailedException e) {
        log.debug("Получен статус 400 Bad request (ConversionFailedException): {}", e.getMessage());
        return new ErrorResponse(BAD_REQUEST, "Incorrectly made request", e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse onUnexpectedError(Throwable e) {
        log.debug("Получен статус 500 Internal server error {}", e.getMessage(), e);
        return new ErrorResponse(INTERNAL_SERVER_ERROR, "Incorrectly made request", e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }
}
