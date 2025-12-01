package dev.kuchishkin.model;

import dev.kuchishkin.enums.EventStatus;
import java.time.LocalDateTime;
import java.util.List;

public record EventChangeKafkaMessage(
    Long eventId,
    Long ownerId,
    Long changedById,
    List<Long> users,
    FieldModification<String> name,
    FieldModification<Integer> maxPlaces,
    FieldModification<LocalDateTime> date,
    FieldModification<Integer> cost,
    FieldModification<Integer> duration,
    FieldModification<Long> locationId,
    FieldModification<EventStatus> eventStatus
) {

}
