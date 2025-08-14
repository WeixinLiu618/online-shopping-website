package com.shop.orderservice.kafka;

import com.shop.orderservice.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(String topic, OrderEvent event) {
        kafkaTemplate.send(topic, event.getOrderId().toString(), event);
    }
}

