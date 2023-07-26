package ru.practicum.ApiError.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class BadRequestException  extends RuntimeException {
    private final HttpStatus status;

    private final String message;
    private final String reason;
    private final LocalDateTime timestamp;

    public BadRequestException(HttpStatus status, String reason, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.reason = reason;
        this.timestamp = timestamp;
    }

}