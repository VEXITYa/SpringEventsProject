package dev.kuchishkin.service;

import dev.kuchishkin.entity.EventEntity;
import dev.kuchishkin.enums.EventStatus;
import dev.kuchishkin.kafka.EventChangeEventSender;
import dev.kuchishkin.model.EventChangeKafkaMessage;
import dev.kuchishkin.model.FieldModification;
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
    private final EventChangeEventSender eventSender;

    public EventStatusUpdateScheduledService(EventRepository eventRepository,
        EventChangeEventSender eventSender) {
        this.eventRepository = eventRepository;
        this.eventSender = eventSender;
    }

    @Scheduled(cron = "${event.updateEventStats.cron}")
    public void updateEventStats() {
        log.info("Starting Event Status Update Scheduled Service");

        var startedEvents = eventRepository.findStartedEventsWithStatus(EventStatus.WAIT_START);
        startedEvents.forEach(
            eventId -> {
                eventSender.sendEvent(
                    eventStatusChanged(
                        eventRepository.findById(eventId).get(),
                        EventStatus.STARTED
                    )
                );
                eventRepository.changeEventStatus(eventId, EventStatus.STARTED);
            });

        var finishedEvents = eventRepository.findFinishedEventsWithStatus(EventStatus.STARTED);
        finishedEvents.forEach(
            eventId -> {
                eventSender.sendEvent(
                    eventStatusChanged(
                        eventRepository.findById(eventId).get(),
                        EventStatus.FINISHED
                    )
                );
                eventRepository.changeEventStatus(eventId, EventStatus.FINISHED);
            }
        );
    }

    private EventChangeKafkaMessage eventStatusChanged(EventEntity event,
        EventStatus newEventStatus) {
        return new EventChangeKafkaMessage(
            event.getId(),
            event.getOwnerId(),
            null,
            event.getRegistrationList()
                .stream()
                .map(reg -> reg.getUserId())
                .toList(),
            null,
            null,
            null,
            null,
            null,
            null,
            new FieldModification<>(event.getStatus(), newEventStatus)
        );
    }
}
