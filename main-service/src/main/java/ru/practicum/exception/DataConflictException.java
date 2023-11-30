package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DataConflictException extends RuntimeException {
    public DataConflictException(String message) {
        super(message);
    }
}
