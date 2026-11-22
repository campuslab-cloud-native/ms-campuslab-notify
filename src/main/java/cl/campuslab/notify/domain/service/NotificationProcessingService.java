package cl.campuslab.notify.domain.service;

import cl.campuslab.notify.domain.model.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationProcessingService {

    public void processEmail(NotificationMessage message) {
        validate(message);

        log.info(
                "Processing email notification. eventId={}, type={}",
                message.getEventId(),
                message.getType()
        );
    }

    public void processPreparation(NotificationMessage message) {
        validate(message);

        log.info(
                "Processing preparation notification. eventId={}, type={}",
                message.getEventId(),
                message.getType()
        );
    }

    public void processVoucher(NotificationMessage message) {
        validate(message);

        log.info(
                "Processing voucher notification. eventId={}, type={}",
                message.getEventId(),
                message.getType()
        );
    }

    private void validate(NotificationMessage message) {

        if (message == null) {
            throw new IllegalArgumentException("Notification message cannot be null");
        }

        if (message.getEventId() == null || message.getEventId().isBlank()) {
            throw new IllegalArgumentException("eventId is required");
        }

        if (message.getType() == null || message.getType().isBlank()) {
            throw new IllegalArgumentException("type is required");
        }

        if (message.getPayload() == null) {
            throw new IllegalArgumentException("payload is required");
        }
    }
}