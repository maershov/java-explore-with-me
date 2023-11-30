package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice(basePackages = "ru.practicum")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidStatusException(final InvalidStatusException e) {
        log.error("Ошибка при обработке недопустимого статуса: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse ifDataException(final DataConflictException e) {
        log.error("Ошибка данных: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFoundExceptionHandler(final EntityNotFoundException e) {
        log.error("Сущность не найдена: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentExceptionHandler(final IllegalArgumentException e) {
        log.error("Ошибка недопустимых аргументов: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse dataIntegrityViolationExceptionHandler(final DataIntegrityViolationException e) {
        log.error("Ошибка целостности данных: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse ifTypeMismatchException(final TypeMismatchException e) {
        log.error("Ошибка несоответствия типов: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandler(final MethodArgumentNotValidException e) {
        log.error("Ошибка при валидации аргументов метода: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationExceptionHandler(final ConstraintViolationException e) {
        log.error("Ошибка нарушения ограничений: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
