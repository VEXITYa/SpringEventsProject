package dev.kuchishkin.kafka;

import dev.kuchishkin.model.EventChangeKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventChangeEventSender {
    private static final Logger log = LoggerFactory.getLogger(EventChangeEventSender.class);

    private final KafkaTemplate<Long, EventChangeKafkaMessage> kafkaTemplate;

    public EventChangeEventSender(KafkaTemplate<Long, EventChangeKafkaMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(EventChangeKafkaMessage bookKafkaEvent) {
        log.info("Sending event: event={}", bookKafkaEvent);
        var result = kafkaTemplate.send(
            "event-change-topic",
            bookKafkaEvent.eventId(),
            bookKafkaEvent
        );

        result.thenAccept(sendResult -> {
            log.info("Send successful");
        });
    }
}
