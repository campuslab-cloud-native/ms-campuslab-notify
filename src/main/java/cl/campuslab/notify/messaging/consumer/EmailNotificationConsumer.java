package cl.campuslab.notify.messaging.consumer;

import cl.campuslab.notify.config.MessagingNames;
import cl.campuslab.notify.domain.model.NotificationMessage;
import cl.campuslab.notify.domain.service.NotificationProcessingService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailNotificationConsumer extends AbstractNotificationConsumer {

    public EmailNotificationConsumer(NotificationProcessingService processingService) {
        super(processingService);
    }

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
}
