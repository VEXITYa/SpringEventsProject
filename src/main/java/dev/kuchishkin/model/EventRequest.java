package dev.kuchishkin.model;

import java.time.LocalDateTime;

public record EventRequest(
    String name,
    int maxPlaces,
    LocalDateTime date,
    int cost,
    int duration,
    Long locationId
) {

}
