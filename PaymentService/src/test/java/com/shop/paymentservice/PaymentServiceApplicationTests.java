package com.shop.paymentservice;

import com.shop.paymentservice.event.PaymentEvent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class PaymentServiceApplicationTests {

	@MockBean
	private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

	@Test
	void contextLoads() {
	}

}
