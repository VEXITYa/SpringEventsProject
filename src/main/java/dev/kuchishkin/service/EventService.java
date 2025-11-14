package dev.kuchishkin.service;

import dev.kuchishkin.dto.EventCreateDto;
import dev.kuchishkin.dto.EventSearchFilter;
import dev.kuchishkin.dto.EventUpdateDto;
import dev.kuchishkin.entity.EventEntity;
import dev.kuchishkin.entity.LocationEntity;
import dev.kuchishkin.entity_converters.EventEntityConverter;
import dev.kuchishkin.entity_converters.LocationEntityConverter;
import dev.kuchishkin.enums.EventStatus;
import dev.kuchishkin.enums.UserRole;
import dev.kuchishkin.model.Event;
import dev.kuchishkin.model.User;
import dev.kuchishkin.repository.EventRepository;
import dev.kuchishkin.security.jwt.JwtAuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private final static Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final LocationEntityConverter locationEntityConverter;
    private final EventEntityConverter eventEntityConverter;

    public EventService(
        EventRepository eventRepository,
        LocationService locationService,
        JwtAuthenticationService jwtAuthenticationService,
        LocationEntityConverter locationEntityConverter,
        EventEntityConverter eventEntityConverter
    ) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.locationEntityConverter = locationEntityConverter;
        this.eventEntityConverter = eventEntityConverter;
    }

    public Event findById(Long id) {
        EventEntity event = eventRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Event not found by id = %s".formatted(id))
        );
        return eventEntityConverter.toModel(event);
    }

    public void checkUserAuthorities(Long eventId) {
        User currentUser = jwtAuthenticationService.getCurrentUser();
        Event event = findById(eventId);
        if (!(event.owner_id().equals(currentUser.id()) || currentUser.role()
            .equals(UserRole.ADMIN))) {
            throw new IllegalArgumentException(
                "This user dont have authorities to modify this event");
        }
    }

    public Event create(EventCreateDto eventCreate) {
        var location = locationService.findById(eventCreate.locationId());

        if (location.capacity() < eventCreate.maxPlaces()) {
            log.error(
                "Capacity overflow. Capacity of location is: %s".formatted(location.capacity()));
            throw new IllegalArgumentException(
                "Capacity overflow. Capacity of location is: %s".formatted(location.capacity()));
        }

        User currentUser = jwtAuthenticationService.getCurrentUser();

        EventEntity eventEntity = new EventEntity(
            null,
            eventCreate.name(),
            currentUser.id(),
            eventCreate.maxPlaces(),
            eventCreate.date(),
            eventCreate.cost(),
            eventCreate.duration(),
            locationEntityConverter.toEntity(location),
            EventStatus.WAIT_START,
            List.of()
        );

        eventEntity = eventRepository.save(eventEntity);
        log.info("Event created successfully: eventId = {}", eventEntity.getId());

        return eventEntityConverter.toModel(eventEntity);
    }

    @Transactional
    public Event update(Long id, EventUpdateDto eventUpdate) {
        checkUserAuthorities(id);
        EventEntity event = eventRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Event not found")
        );

        LocationEntity location = event.getLocation();

        if (eventUpdate.locationId() != null && !eventUpdate.locationId()
            .equals(event.getLocation().getId())) {
            location = locationEntityConverter.toEntity(
                locationService.findById(eventUpdate.locationId()));
        }

        if (eventUpdate.maxPlaces() < event.getMaxPlaces()) {
            throw new IllegalArgumentException(
                "Capacity should be greater than or equal to max places");
        }

        if (eventUpdate.maxPlaces() > location.getCapacity()) {
            throw new IllegalArgumentException(
                "Capacity should be lower than or equal to location capacity");
        }

        if (!event.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException(
                "Cannot modify event is status: %s".formatted(event.getStatus()));
        }

        Optional.ofNullable(eventUpdate.name()).ifPresent(event::setName);
        Optional.of(eventUpdate.duration()).ifPresent(event::setDuration);
        Optional.ofNullable(eventUpdate.date()).ifPresent(event::setDate);
        Optional.of(eventUpdate.cost()).ifPresent(event::setCost);
        Optional.of(eventUpdate.maxPlaces()).ifPresent(event::setMaxPlaces);
        Optional.of(location).ifPresent(event::setLocation);

        eventRepository.save(event);
        log.info("Event updated successfully: eventId = {}", id);
        return eventEntityConverter.toModel(event);
    }

    public void cancelEvent(Long eventId) {
        checkUserAuthorities(eventId);
        Event event = findById(eventId);

        if (event.status().equals(EventStatus.CANCELLED)) {
            log.info("Event already cancelled: eventId = {}", eventId);
            return;
        }
        if (event.status().equals(EventStatus.STARTED) || event.status()
            .equals(EventStatus.FINISHED)) {
            throw new IllegalArgumentException(
                "Cannot cancel an event because it is already in progress");
        }

        eventRepository.changeEventStatus(eventId, EventStatus.CANCELLED);
        log.info("Event cancelled successfully: eventId = {}", eventId);
    }

    public List<Event> findAuthenticatedUserEvents() {
        User user = jwtAuthenticationService.getCurrentUser();
        List<EventEntity> events = eventRepository.findAllByOwnerId(user.id());

        return events.stream().map(eventEntityConverter::toModel).toList();
    }

    public List<Event> findByFilter(EventSearchFilter filter) {
        List<EventEntity> found = eventRepository.findEvents(
            filter.name(),
            filter.placesMin(),
            filter.placesMax(),
            filter.dateAfter(),
            filter.dateBefore(),
            filter.costMin(),
            filter.costMax(),
            filter.durationMin(),
            filter.durationMax(),
            filter.locationId(),
            filter.eventStatus()
        );

        return found.stream().map(eventEntityConverter::toModel).toList();
    }
}
