package ru.practicum.ApiError;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ErrorResponse badRequestException (BadRequestException e) {
        return new ErrorResponse(e.getStatus(),
                e.getReason(),
                e.getMessage(),
                e.getTimestamp());
    }
}