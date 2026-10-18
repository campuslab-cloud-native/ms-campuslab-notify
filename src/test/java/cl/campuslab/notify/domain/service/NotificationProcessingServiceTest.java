package cl.campuslab.notify.domain.service;

import cl.campuslab.notify.domain.model.NotificationMessage;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificationProcessingServiceTest {

    private final NotificationProcessingService processingService =
            new NotificationProcessingService();

    @Test
    void processesEmailPreparationAndVoucherMessages() {
        NotificationMessage notification = NotificationMessage.builder()
                .type("COMMAND")
                .eventId("event-1")
                .payload(Map.of())
                .build();

        assertDoesNotThrow(() -> processingService.processEmail(notification));
        assertDoesNotThrow(() -> processingService.processPreparation(notification));
        assertDoesNotThrow(() -> processingService.processVoucher(notification));
    }

    @Test
    void rejectsMessagesMissingRequiredFields() {
        NotificationMessage notification = NotificationMessage.builder()
                .type("COMMAND")
                .eventId(" ")
                .payload(Map.of())
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> processingService.processEmail(notification)
        );
    }
}
