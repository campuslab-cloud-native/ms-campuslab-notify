package cl.campuslab.notify.messaging.consumer;

import cl.campuslab.notify.domain.model.NotificationMessage;
import cl.campuslab.notify.domain.service.NotificationProcessingService;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.IOException;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NotificationConsumerTest {

    @Test
    void acknowledgesSuccessfullyProcessedMessage() throws IOException {
        NotificationProcessingService processingService =
                mock(NotificationProcessingService.class);
        NotificationConsumer consumer = new NotificationConsumer(processingService);
        Channel channel = mock(Channel.class);
        NotificationMessage notification = validNotification();
        Message message = amqpMessage(42L, false);

        consumer.consumeEmailCommand(notification, message, channel);

        verify(processingService).processEmail(notification);
        verify(channel).basicAck(42L, false);
    }

    @Test
    void rejectsInvalidMessageWithoutRequeue() throws IOException {
        NotificationConsumer consumer =
                new NotificationConsumer(new NotificationProcessingService());
        Channel channel = mock(Channel.class);

        consumer.consumeEmailCommand(null, amqpMessage(43L, false), channel);

        verify(channel).basicNack(43L, false, false);
    }

    @Test
    void retriesOnceThenRejectsRedeliveredMessage() throws IOException {
        NotificationProcessingService processingService =
                mock(NotificationProcessingService.class);
        doThrow(new IllegalStateException("processing failed"))
                .when(processingService)
                .processEmail(any());
        NotificationConsumer consumer = new NotificationConsumer(processingService);
        Channel channel = mock(Channel.class);
        NotificationMessage notification = validNotification();

        consumer.consumeEmailCommand(notification, amqpMessage(44L, false), channel);
        consumer.consumeEmailCommand(notification, amqpMessage(45L, true), channel);

        verify(channel).basicNack(44L, false, true);
        verify(channel).basicNack(45L, false, false);
    }

    private NotificationMessage validNotification() {
        return NotificationMessage.builder()
                .type("EMAIL")
                .eventId("event-1")
                .payload(Map.of())
                .build();
    }

    private Message amqpMessage(long deliveryTag, boolean redelivered) {
        MessageProperties properties = new MessageProperties();
        properties.setDeliveryTag(deliveryTag);
        properties.setRedelivered(redelivered);
        return new Message(new byte[0], properties);
    }
}
