package cl.campuslab.notify.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessagingNamesTest {

    @Test
    void containsOnlyTheQueuesConsumedByNotify() {
        assertEquals("q.cmd.email", MessagingNames.EMAIL_QUEUE);
        assertEquals("q.cmd.prep", MessagingNames.PREP_QUEUE);
        assertEquals("q.cmd.voucher", MessagingNames.VOUCHER_QUEUE);
    }
}
