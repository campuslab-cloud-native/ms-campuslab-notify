package cl.campuslab.notify.config;

public final class MessagingNames {

    private MessagingNames() {
    }

    // Exchanges
    public static final String NOTIFY_EXCHANGE = "x.cmd.notify";
    public static final String NOTIFY_DLX = "x.dlx.notify";

    // Queues
    public static final String EMAIL_QUEUE = "q.cmd.email";
    public static final String PREP_QUEUE = "q.cmd.prep";
    public static final String VOUCHER_QUEUE = "q.cmd.voucher";

    // Dead Letter Queues
    public static final String EMAIL_DLQ = "q.dlq.email";
    public static final String PREP_DLQ = "q.dlq.prep";
    public static final String VOUCHER_DLQ = "q.dlq.voucher";

    // Routing keys
    public static final String EMAIL_ROUTING_KEY = "cmd.email";
    public static final String PREP_ROUTING_KEY = "cmd.prep";
    public static final String VOUCHER_ROUTING_KEY = "cmd.voucher";

    // Dead letter routing keys
    public static final String EMAIL_DLQ_ROUTING_KEY = "dlq.email";
    public static final String PREP_DLQ_ROUTING_KEY = "dlq.prep";
    public static final String VOUCHER_DLQ_ROUTING_KEY = "dlq.voucher";
}
