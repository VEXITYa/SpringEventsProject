package dev.kuchishkin.dto_converters;

import dev.kuchishkin.dto.EventCreateDto;
import dev.kuchishkin.dto.EventUpdateDto;
import dev.kuchishkin.model.EventRequest;
import org.springframework.stereotype.Component;

@Component
public class EventRequestEntityConverter {
    public EventRequest toModel(EventUpdateDto eventUpdate) {
        return new EventRequest(
            eventUpdate.name(),
            eventUpdate.maxPlaces(),
            eventUpdate.date(),
            eventUpdate.cost(),
            eventUpdate.duration(),
            eventUpdate.locationId()
        );
    }

    public EventRequest toModel(EventCreateDto eventUpdate) {
        return new EventRequest(
            eventUpdate.name(),
            eventUpdate.maxPlaces(),
            eventUpdate.date(),
            eventUpdate.cost(),
            eventUpdate.duration(),
            eventUpdate.locationId()
        );
    }
}
