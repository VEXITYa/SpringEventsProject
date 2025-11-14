package dev.kuchishkin.entity_converters;

import dev.kuchishkin.entity.EventEntity;
import dev.kuchishkin.model.Event;
import dev.kuchishkin.model.EventRegistration;
import org.springframework.stereotype.Component;

@Component
public class EventEntityConverter {

    private final LocationEntityConverter locationEntityConverter;

    public EventEntityConverter(LocationEntityConverter locationEntityConverter) {
        this.locationEntityConverter = locationEntityConverter;
    }

    public Event toModel(EventEntity entity) {
        return new Event(
            entity.getId(),
            entity.getName(),
            entity.getOwnerId(),
            entity.getMaxPlaces(),
            entity.getDate(),
            entity.getCost(),
            entity.getDuration(),
            locationEntityConverter.toModel(entity.getLocation()),
            entity.getStatus(),
            entity.getRegistrationList().stream()
                .map(it -> new EventRegistration(
                    it.getId(),
                    it.getUserId(),
                    entity.getId())
                )
                .toList()
        );
    }

}
