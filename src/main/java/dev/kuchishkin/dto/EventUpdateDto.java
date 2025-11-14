package dev.kuchishkin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

public record EventUpdateDto(
    String name,

    @Positive(message = "Maximum places must be positive")
    int maxPlaces,

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Future(message = "Date must be in future")
    LocalDateTime date,

    @PositiveOrZero(message = "Cost must be non-negative")
    int cost,

    @Min(value = 30, message = "Duration must be at least 30 minutes")
    int duration,

    Long locationId
) {

}
