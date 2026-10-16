package cl.campuslab.notify.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitConfig {

    @Bean
    public DirectExchange notifyExchange() {
        return new DirectExchange(MessagingNames.NOTIFY_EXCHANGE);
    }

    @Bean
    public DirectExchange notifyDeadLetterExchange() {
        return new DirectExchange(MessagingNames.NOTIFY_DLX);
    }

    // =========================
    // MAIN QUEUES
    // =========================

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(MessagingNames.EMAIL_QUEUE)
                .deadLetterExchange(MessagingNames.NOTIFY_DLX)
                .deadLetterRoutingKey(MessagingNames.EMAIL_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue preparationQueue() {
        return QueueBuilder.durable(MessagingNames.PREP_QUEUE)
                .deadLetterExchange(MessagingNames.NOTIFY_DLX)
                .deadLetterRoutingKey(MessagingNames.PREP_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue voucherQueue() {
        return QueueBuilder.durable(MessagingNames.VOUCHER_QUEUE)
                .deadLetterExchange(MessagingNames.NOTIFY_DLX)
                .deadLetterRoutingKey(MessagingNames.VOUCHER_DLQ_ROUTING_KEY)
                .build();
    }

    // =========================
    // DLQ
    // =========================

    @Bean
    public Queue emailDeadLetterQueue() {
        return QueueBuilder.durable(MessagingNames.EMAIL_DLQ).build();
    }

    @Bean
    public Queue preparationDeadLetterQueue() {
        return QueueBuilder.durable(MessagingNames.PREP_DLQ).build();
    }

    @Bean
    public Queue voucherDeadLetterQueue() {
        return QueueBuilder.durable(MessagingNames.VOUCHER_DLQ).build();
    }

    // =========================
    // MAIN BINDINGS
    // =========================

    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(notifyExchange())
                .with(MessagingNames.EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding preparationBinding() {
        return BindingBuilder
                .bind(preparationQueue())
                .to(notifyExchange())
                .with(MessagingNames.PREP_ROUTING_KEY);
    }

    @Bean
    public Binding voucherBinding() {
        return BindingBuilder
                .bind(voucherQueue())
                .to(notifyExchange())
                .with(MessagingNames.VOUCHER_ROUTING_KEY);
    }

    // =========================
    // DLQ BINDINGS
    // =========================

    @Bean
    public Binding emailDeadLetterBinding() {
        return BindingBuilder
                .bind(emailDeadLetterQueue())
                .to(notifyDeadLetterExchange())
                .with(MessagingNames.EMAIL_DLQ_ROUTING_KEY);
    }

    @Bean
    public Binding preparationDeadLetterBinding() {
        return BindingBuilder
                .bind(preparationDeadLetterQueue())
                .to(notifyDeadLetterExchange())
                .with(MessagingNames.PREP_DLQ_ROUTING_KEY);
    }

    @Bean
    public Binding voucherDeadLetterBinding() {
        return BindingBuilder
                .bind(voucherDeadLetterQueue())
                .to(notifyDeadLetterExchange())
                .with(MessagingNames.VOUCHER_DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter(JsonMapper objectMapper) {
        return new JacksonJsonMessageConverter(objectMapper);
    }
}