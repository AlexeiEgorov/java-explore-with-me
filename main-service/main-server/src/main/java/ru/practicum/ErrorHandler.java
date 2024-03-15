package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.model.ConstraintViolationException;
import ru.practicum.model.ErrorResponse;
import ru.practicum.model.NotAllowedActionException;

import java.time.LocalDateTime;
import java.util.Arrays;

import static ru.practicum.Constants.*;

@Slf4j
@RestControllerAdvice("ru.practicum")
public class ErrorHandler {
    private static final String ENTITY_NOT_FOUND = "%s with id=%d was not found";
    private static final String NOT_FOUND = "NOT_FOUND";
    private static final String REQ_OBJ_NOT_FOUND = "The required object was not found.";

    @ExceptionHandler({NotAllowedActionException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onNotAllowedActionException(RuntimeException e) {
        log.debug("Получен статус 400 Bad request; value:{}", e.getMessage(), e);
        return new ErrorResponse(BAD_REQUEST, INCORR_MADE_REQ, e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse onEntityNotFoundException(EntityNotFoundException e) {
        log.debug("Получен статус 404 Not found; value:{}", e.getMessage(), e);
        return new ErrorResponse(NOT_FOUND, REQ_OBJ_NOT_FOUND,
                String.format(ENTITY_NOT_FOUND, e.getEntityClass(), e.getValue()),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse onDataIntegrityViolationException(RuntimeException e) {
        log.debug("Получен статус 409 Conflict; {}", e.getMessage(), e);
        return new ErrorResponse(CONFLICT, CONS_VIOL, e.getMessage(), LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse onUnexpectedError(Throwable e) {
        log.debug("Получен статус 500 Internal server error {}", e.getMessage(), e);
        return new ErrorResponse(INTERNAL_SERVER_ERROR, Arrays.toString(e.getStackTrace()), e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }
}
