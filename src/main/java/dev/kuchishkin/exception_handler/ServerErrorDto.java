package dev.kuchishkin.exception_handler;


import java.time.LocalDateTime;


public record ServerErrorDto(
    String message,
    String detailedMessage,
    LocalDateTime dateTime
) {

}
