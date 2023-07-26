package ru.practicum.ApiError;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ApiError.exception.BadRequestException;


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
