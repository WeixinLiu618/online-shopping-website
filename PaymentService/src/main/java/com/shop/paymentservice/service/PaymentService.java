package com.shop.paymentservice.service;

import com.shop.paymentservice.entity.Payment;
import com.shop.paymentservice.event.OrderCreatedEvent;

import java.util.Optional;
import java.util.UUID;

public interface PaymentService {
    void handleOrderCreatedEvent(OrderCreatedEvent event);
    Optional<Payment> submitPayment(UUID orderId);
    boolean refundPayment(UUID orderId);
    Optional<Payment> lookupPayment(UUID orderId);
}
