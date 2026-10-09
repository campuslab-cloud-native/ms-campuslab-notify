package cl.campuslab.notify.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EMAIL_QUEUE = "q.cmd.email";
    public static final String PREP_QUEUE = "q.cmd.prep";
    public static final String VOUCHER_QUEUE = "q.cmd.voucher";

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public Queue preparationQueue() {
        return new Queue(PREP_QUEUE, true);
    }

    @Bean
    public Queue voucherQueue() {
        return new Queue(VOUCHER_QUEUE, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new JacksonJsonMessageConverter(objectMapper);
    }
}