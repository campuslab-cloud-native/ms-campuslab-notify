package cl.campuslab.notify.domain.service;

import cl.campuslab.notify.domain.model.NotificationMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(OutputCaptureExtension.class)
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

    @Test
    void doesNotWriteNotificationPayloadToLogs(CapturedOutput output) {
        String sensitiveValue = "private-user-marker";
        NotificationMessage notification = NotificationMessage.builder()
                .type("COMMAND")
                .eventId("event-2")
                .payload(Map.of("userId", sensitiveValue))
                .build();

        processingService.processEmail(notification);
        processingService.processPreparation(notification);
        processingService.processVoucher(notification);

        assertFalse(output.getAll().contains(sensitiveValue));
    }
}
