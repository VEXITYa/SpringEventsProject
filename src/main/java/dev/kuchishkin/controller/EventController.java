package dev.kuchishkin.controller;

import dev.kuchishkin.dto.EventCreateDto;
import dev.kuchishkin.dto.EventDto;
import dev.kuchishkin.dto.EventSearchFilter;
import dev.kuchishkin.dto.EventUpdateDto;
import dev.kuchishkin.dto_converters.EventDtoConverter;
import dev.kuchishkin.service.EventService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;
    private final EventDtoConverter eventDtoConverter;

    public EventController(
        EventService eventService,
        EventDtoConverter eventDtoConverter
    ) {
        this.eventService = eventService;
        this.eventDtoConverter = eventDtoConverter;
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody @Valid EventCreateDto eventDto) {
        log.info("Post request createEvent: event = {}", eventDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(eventDtoConverter.toDto(eventService.create(eventDto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        log.info("Get request getEvent: id = {}", id);

        return ResponseEntity.ok().body(eventDtoConverter.toDto(eventService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        log.info("Delete request deleteEvent: id = {}", id);

        eventService.cancelEvent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(
        @PathVariable Long id,
        @RequestBody @Valid EventUpdateDto eventDto
    ) {
        log.info("Update request updateEvent: eventId = {}", id);

        var event = eventService.update(id, eventDto);

        return ResponseEntity.status(HttpStatus.OK).body(eventDtoConverter.toDto(event));
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyEvents() {
        log.info("Get request getMyEvents");

        return ResponseEntity.ok().body(
            eventService
                .findAuthenticatedUserEvents()
                .stream()
                .map(eventDtoConverter::toDto)
                .toList()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDto>> searchEvents(
        @RequestBody @Valid EventSearchFilter filter
    ) {
        log.info("Get request searchEvents: filter = {}", filter);

        return ResponseEntity.ok().body(
            eventService
                .findByFilter(filter)
                .stream()
                .map(eventDtoConverter::toDto)
                .toList()
        );
    }
}
