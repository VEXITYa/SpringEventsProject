package dev.kuchishkin.dto_converters;

import dev.kuchishkin.dto.EventDto;
import dev.kuchishkin.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventDtoConverter {

    public EventDto toDto(Event event) {
        return new EventDto(
            event.id(),
            event.name(),
            event.owner_id(),
            event.maxPlaces(),
            event.date(),
            event.cost(),
            event.duration(),
            event.location().id(),
            event.status(),
            event.registrationList().size()
        );
    }
}
