package dev.kuchishkin.controller;

import dev.kuchishkin.dto.EventDto;
import dev.kuchishkin.dto_converters.EventDtoConverter;
import dev.kuchishkin.security.jwt.JwtAuthenticationService;
import dev.kuchishkin.service.EventRegistrationService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events/registrations")
public class EventRegistrationController {

    private static final Logger log = LoggerFactory.getLogger(
        EventRegistrationController.class.getName());

    private final EventRegistrationService eventRegistrationService;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final EventDtoConverter eventDtoConverter;

    public EventRegistrationController(
        EventRegistrationService eventRegistrationService,
        JwtAuthenticationService jwtAuthenticationService,
        EventDtoConverter eventDtoConverter) {
        this.eventRegistrationService = eventRegistrationService;
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.eventDtoConverter = eventDtoConverter;
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> registerUserToEvent(@PathVariable Long eventId) {
        log.info("Post request createEventRegistration: eventId = {}", eventId);

        eventRegistrationService.registerUserOnEvent(jwtAuthenticationService.getCurrentUser(),
            eventId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> cancelUserRegistrationToEvent(@PathVariable Long eventId) {
        log.info("Delete request deleteEventRegistration: eventId = {}", eventId);

        eventRegistrationService.cancelUserRegistrationOnEvent(
            jwtAuthenticationService.getCurrentUser(), eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyEventRegistrations() {
        log.info("Get request getMyEventRegistrations");

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                eventRegistrationService
                    .findUserEventRegistrations(
                        jwtAuthenticationService.getCurrentUser()
                    ).stream()
                    .map(eventDtoConverter::toDto)
                    .toList()
            );
    }
}
