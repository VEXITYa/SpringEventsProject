package dev.kuchishkin.service;

import dev.kuchishkin.entity.EventRegistrationEntity;
import dev.kuchishkin.entity_converters.EventEntityConverter;
import dev.kuchishkin.enums.EventStatus;
import dev.kuchishkin.model.Event;
import dev.kuchishkin.model.User;
import dev.kuchishkin.repository.EventRegistrationRepository;
import dev.kuchishkin.repository.EventRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(EventRegistrationService.class);

    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventService eventService;
    private final EventRepository eventRepository;
    private final EventEntityConverter eventEntityConverter;

    public EventRegistrationService(
        EventRegistrationRepository eventRegistrationRepository,
        EventService eventService,
        EventRepository eventRepository,
        EventEntityConverter eventEntityConverter
    ) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.eventEntityConverter = eventEntityConverter;
    }

    public void registerUserOnEvent(User user, Long eventId) {
        Event event = eventService.findById(eventId);

        if (!event.status().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException(
                "You can't register a user after the event is started. Event status: %s".formatted(
                    event.status()));
        }

        if (user.id().equals(event.owner_id())) {
            throw new IllegalArgumentException("Owner of event cannot register on his event");
        }

        var registration = eventRegistrationRepository.findEventRegistration(user.id(), eventId);
        if (registration.isPresent()) {
            throw new IllegalArgumentException("Event registration already exists");
        }

        if (event.registrationList().size() >= event.maxPlaces()) {
            throw new IllegalArgumentException("Maximum number of places reached");
        }

        eventRegistrationRepository.save(
            new EventRegistrationEntity(
                null,
                user.id(),
                eventRepository.findById(eventId).orElseThrow()
            )
        );
    }

    public void cancelUserRegistrationOnEvent(User user, Long eventId) {
        Event event = eventService.findById(eventId);
        var registration = eventRegistrationRepository.findEventRegistration(user.id(), eventId);
        if (registration.isEmpty()) {
            throw new IllegalArgumentException("Event registration not found");
        }
        if (!event.status().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException(
                "You can't cancel registration after the event is started. Event status: %s".formatted(
                    event.status()));
        }

        eventRegistrationRepository.delete(registration.get());
    }

    public List<Event> findUserEventRegistrations(User currentUser) {
        return eventRegistrationRepository
            .findUserEventRegistrations(currentUser.id())
            .stream()
            .map(eventEntityConverter::toModel)
            .toList();
    }
}
