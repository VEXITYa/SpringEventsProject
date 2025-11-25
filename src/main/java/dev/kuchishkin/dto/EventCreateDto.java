package dev.kuchishkin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

public record EventCreateDto(

    @NotBlank(message = "Name required")
    String name,

    @Positive(message = "Maximum places must be positive")
    Integer maxPlaces,

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Future(message = "Date must be in future")
    LocalDateTime date,

    @PositiveOrZero(message = "Cost must be non-negative")
    Integer cost,

    @Min(value = 30, message = "Duration must be at least 30 minutes")
    Integer duration,

    @NotNull(message = "Location ID required")
    Long locationId
) {

}
