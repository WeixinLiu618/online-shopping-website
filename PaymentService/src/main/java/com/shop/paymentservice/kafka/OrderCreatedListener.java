package com.shop.paymentservice.kafka;


import com.shop.paymentservice.event.OrderEvent;
import com.shop.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.shop.paymentservice.event.KafkaTopics;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final PaymentService paymentService;

    @KafkaListener(topics = KafkaTopics.ORDER_EVENT, groupId = "payment-service")
    public void handleOrderCreated(OrderEvent event) {
        log.info("Received order created event: {}", event);
        paymentService.handleOrderCreatedEvent(event);
    }
}

