package ms_campuslab_notify;

import cl.campuslab.notify.NotifyApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		classes = NotifyApplication.class,
		properties = "spring.rabbitmq.listener.simple.auto-startup=false"
)
class MsCampuslabNotifyApplicationTests {

	@Test
	void contextLoads() {
	}

}
