package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.model.ErrorResponse;

import java.time.LocalDateTime;
import java.util.Arrays;

import static ru.practicum.Constants.*;

@Slf4j
@RestControllerAdvice("ru.practicum")
public class ErrorHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            org.springframework.web.bind.MissingServletRequestParameterException.class,
            ConversionFailedException.class,
            ru.practicum.model.ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onNotAllowedActionException(RuntimeException e) {
        log.debug("Получен статус 400 Bad request; message:{}", e.getMessage(), e);
        return new ErrorResponse(BAD_REQUEST, INCORR_MADE_REQ, e.getMessage(),
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
