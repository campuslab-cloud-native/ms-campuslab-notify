package cl.campuslab.notify.messaging.consumer;

import cl.campuslab.notify.config.MessagingNames;
import cl.campuslab.notify.domain.model.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationConsumer {

    @RabbitListener(queues = MessagingNames.EMAIL_QUEUE)
    public void consumeEmailCommand(NotificationMessage message) {
        log.info(
                "Email command received. eventId={}, type={}, payload={}",
                message.getEventId(),
                message.getType(),
                message.getPayload()
        );
    }

    @RabbitListener(queues = MessagingNames.PREP_QUEUE)
    public void consumePreparationCommand(NotificationMessage message) {
        log.info(
                "Preparation command received. eventId={}, type={}, payload={}",
                message.getEventId(),
                message.getType(),
                message.getPayload()
        );
    }

    @RabbitListener(queues = MessagingNames.VOUCHER_QUEUE)
    public void consumeVoucherCommand(NotificationMessage message) {
        log.info(
                "Voucher command received. eventId={}, type={}, payload={}",
                message.getEventId(),
                message.getType(),
                message.getPayload()
        );
    }
}