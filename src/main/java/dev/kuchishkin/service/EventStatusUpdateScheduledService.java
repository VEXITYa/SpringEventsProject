package dev.kuchishkin.service;

import dev.kuchishkin.enums.EventStatus;
import dev.kuchishkin.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class EventStatusUpdateScheduledService {

    private final static Logger log = LoggerFactory.getLogger(
        EventStatusUpdateScheduledService.class);

    private final EventRepository eventRepository;

    public EventStatusUpdateScheduledService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = "${event.updateEventStats.cron}")
    public void updateEventStats() {
        log.info("Starting Event Status Update Scheduled Service");

        var startedEvents = eventRepository.findStartedEventsWithStatus(EventStatus.WAIT_START);
        startedEvents.forEach(
            eventId -> eventRepository.changeEventStatus(eventId, EventStatus.STARTED));

        var finishedEvents = eventRepository.findFinishedEventsWithStatus(EventStatus.STARTED);
        finishedEvents.forEach(
            eventId -> eventRepository.changeEventStatus(eventId, EventStatus.FINISHED));
    }
}
