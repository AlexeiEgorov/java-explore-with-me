package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.model.ErrorResponse;

@Slf4j
@RestControllerAdvice("ru.practicum")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onIllegalArgumentException(IllegalArgumentException e) {
        log.debug("Получен статус 400 Bad request (IllegalArgumentException): {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onMissingServletRequestParameterException(
            org.springframework.web.bind.MissingServletRequestParameterException e) {
        log.debug("Получен статус 400 Bad request (MissingServletRequestParameterException): {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), "");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onConversionFailedException(ConversionFailedException e) {
        log.debug("Получен статус 400 Bad request (ConversionFailedException): {}", e.getMessage());
        return new ErrorResponse(e.getMessage(), String.valueOf(e.getValue()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onConstraintValidationException(ru.practicum.ConstraintViolationException e) {
        log.debug("Получен статус 400 Conflict (ru.practicum.ConstraintViolationException); value:{}", e.getValue());
        return new ErrorResponse(String.format("Значение ошибки - (%s)", e.getValue()), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse onUnexpectedError(Throwable e) {
        log.debug("Получен статус 500 Internal server error {}", e.getMessage(), e);
        return new ErrorResponse(e.toString(), "возникла непредвиденная ошибка");
    }
}
