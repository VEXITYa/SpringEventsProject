package dev.kuchishkin.dto;

import dev.kuchishkin.enums.EventStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

public record EventDto(
    @NotNull(message = "ID required")
    Long id,

    @NotBlank(message = "Name required")
    String name,

    @NotBlank(message = "Owner ID required")
    Long ownerId,

    @Positive(message = "Maximum places must be positive")
    int maxPlaces,

    @NotNull(message = "Date required")
    LocalDateTime date,

    @PositiveOrZero(message = "Cost must be non-negative")
    int cost,

    @Min(value = 30, message = "Duration must be at least 30 minutes")
    int duration,

    @NotNull(message = "Location ID required")
    Long locationId,

    @NotNull(message = "Status required")
    EventStatus status,

    @PositiveOrZero(message = "Occupied places must be non-negative")
    int occupiedPlaces
) {

}
