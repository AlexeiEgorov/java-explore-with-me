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

import static ru.practicum.Constants.*;

@Slf4j
@RestControllerAdvice("ru.practicum")
public class ErrorHandler {
    private static final String ENTITY_NOT_FOUND = "%s with id=%d was not found";
    private static final String NOT_FOUND = "NOT_FOUND";
    private static final String FORBIDDEN = "FORBIDDEN";
    private static final String REQ_OBJ_NOT_FOUND = "The required object was not found.";
    private static final String INTERNAL_SERV_ERROR = "Internal server error.";
    private static final String INCORR_MADE_REQ = "Incorrectly made request";
    private static final String INTEGRITY_CONS_VIOL = "Integrity constraint has been violated.";
    private static final String CONS_VIOL = "For the requested operation the conditions are not met.";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onNotAllowedActionException(NotAllowedActionException e) {
        log.debug("Получен статус 400 Bad request (NotAllowedActionException); value:{}", e.getValue());
        return new ErrorResponse(BAD_REQUEST, INCORR_MADE_REQ, e.getMessage(),
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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse onEntityNotFoundException(EntityNotFoundException e) {
        log.debug("Получен статус 404 Not found; entityClass:{};value:{}", e.getEntityClass(), e.getValue(), e);
        return new ErrorResponse(NOT_FOUND, REQ_OBJ_NOT_FOUND,
                String.format(ENTITY_NOT_FOUND, e.getEntityClass(), e.getValue()),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        log.debug("Получен статус 409 Conflict (ru.practicum.ConstraintViolationException); value:{}", e.getValue());
        return new ErrorResponse(FORBIDDEN, CONS_VIOL, e.getMessage(), LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse onDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.debug("Получен статус 409 Conflict (); причина: {}", e.toString());
        return new ErrorResponse(CONFLICT, INTEGRITY_CONS_VIOL, e.getMessage(), LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse onUnexpectedError(Throwable e) {
        log.debug("Получен статус 500 Internal server error {}", e.getMessage(), e);
        return new ErrorResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERV_ERROR, e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }
}
