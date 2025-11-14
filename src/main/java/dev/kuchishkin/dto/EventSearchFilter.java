package dev.kuchishkin.dto;

import dev.kuchishkin.enums.EventStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventSearchFilter(
    String name,
    Integer placesMin,
    Integer placesMax,
    LocalDateTime dateAfter,
    LocalDateTime dateBefore,
    BigDecimal costMin,
    BigDecimal costMax,
    Integer durationMin,
    Integer durationMax,
    Integer locationId,
    EventStatus eventStatus
) {

}
