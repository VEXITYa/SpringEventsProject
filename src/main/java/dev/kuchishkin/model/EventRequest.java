package dev.kuchishkin.model;

import java.time.LocalDateTime;

public record EventRequest(
    String name,
    Integer maxPlaces,
    LocalDateTime date,
    Integer cost,
    Integer duration,
    Long locationId
) {

}
