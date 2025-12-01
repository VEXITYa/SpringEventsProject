package dev.kuchishkin.model;

import dev.kuchishkin.enums.EventStatus;
import java.time.LocalDateTime;
import java.util.List;

public record Event(
    Long id,
    String name,
    Long owner_id,
    int maxPlaces,
    LocalDateTime date,
    int cost,
    int duration,
    Location location,
    EventStatus status,
    List<EventRegistration> registrationList
) {

}
