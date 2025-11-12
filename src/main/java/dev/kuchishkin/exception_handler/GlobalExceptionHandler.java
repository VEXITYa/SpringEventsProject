package dev.kuchishkin.exception_handler;


import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ServerErrorDto> handleValidationException(
            Exception e
                                                                   ) {
        log.warn("Got validation exception", e);

        String detailedMessage = e instanceof MethodArgumentNotValidException
                ? constructMethodArgumentNotValidMessage((MethodArgumentNotValidException) e)
                : e.getMessage();

        var errorDto = new ServerErrorDto(
                "Ошибка валидации запроса",
                detailedMessage,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ServerErrorDto> handleGenerisException(
            Exception e
                                                                ) {
        log.error("Server error", e);
        var errorDto = new ServerErrorDto(
                "Server error",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDto> handleNotFoundException(
            EntityNotFoundException e
                                                                 ) {
        log.warn("Got exception", e);
        var errorDto = new ServerErrorDto(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);
    }

    private static String constructMethodArgumentNotValidMessage(
            MethodArgumentNotValidException e
                                                                ) {
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}
