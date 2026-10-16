package cl.campuslab.notify.messaging.consumer;

import cl.campuslab.notify.config.MessagingNames;
import cl.campuslab.notify.domain.model.NotificationMessage;
import cl.campuslab.notify.domain.service.NotificationProcessingService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationProcessingService processingService;

    @RabbitListener(queues = MessagingNames.EMAIL_QUEUE)
    public void consumeEmailCommand(
            NotificationMessage notification,
            Message amqpMessage,
            Channel channel
    ) throws IOException {

        process(
                notification,
                amqpMessage,
                channel,
                () -> processingService.processEmail(notification)
        );
    }

    @RabbitListener(queues = MessagingNames.PREP_QUEUE)
    public void consumePreparationCommand(
            NotificationMessage notification,
            Message amqpMessage,
            Channel channel
    ) throws IOException {

        process(
                notification,
                amqpMessage,
                channel,
                () -> processingService.processPreparation(notification)
        );
    }

    @RabbitListener(queues = MessagingNames.VOUCHER_QUEUE)
    public void consumeVoucherCommand(
            NotificationMessage notification,
            Message amqpMessage,
            Channel channel
    ) throws IOException {

        process(
                notification,
                amqpMessage,
                channel,
                () -> processingService.processVoucher(notification)
        );
    }

    private void process(
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