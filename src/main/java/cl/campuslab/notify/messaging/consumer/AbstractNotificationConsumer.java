package cl.campuslab.notify.messaging.consumer;

import cl.campuslab.notify.domain.model.NotificationMessage;
import cl.campuslab.notify.domain.service.NotificationProcessingService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import java.io.IOException;

@Slf4j
abstract class AbstractNotificationConsumer {

    protected final NotificationProcessingService processingService;

    AbstractNotificationConsumer(NotificationProcessingService processingService) {
        this.processingService = processingService;
    }

    protected void process(
            NotificationMessage notification,
            Message amqpMessage,
            Channel channel,
            Runnable action
    ) throws IOException {

        long deliveryTag =
                amqpMessage.getMessageProperties().getDeliveryTag();

        boolean redelivered =
                amqpMessage.getMessageProperties().isRedelivered();

        try {

            action.run();

            channel.basicAck(deliveryTag, false);

            log.info(
                    "Message acknowledged. eventId={}",
                    notification.getEventId()
            );

        } catch (IllegalArgumentException exception) {

            log.error(
                    "Non recoverable message. Sending to DLQ. eventId={}, error={}",
                    notification != null ? notification.getEventId() : null,
                    exception.getMessage()
            );

            channel.basicNack(
                    deliveryTag,
                    false,
                    false
            );

        } catch (Exception exception) {

            if (!redelivered) {

                log.warn(
                        "Processing failed. Message will be retried once. eventId={}, error={}",
                        notification != null ? notification.getEventId() : null,
                        exception.getMessage()
                );

                channel.basicNack(
                        deliveryTag,
                        false,
                        true
                );

            } else {

                log.error(
                        "Retry failed. Sending message to DLQ. eventId={}, error={}",
                        notification != null ? notification.getEventId() : null,
                        exception.getMessage()
                );

                channel.basicNack(
                        deliveryTag,
                        false,
                        false
                );
            }
        }
    }
}
