package com.shop.paymentservice.service.impl;

import com.shop.paymentservice.entity.Payment;
import com.shop.paymentservice.entity.PaymentMethod;
import com.shop.paymentservice.entity.PaymentStatus;
import com.shop.paymentservice.event.OrderCreatedEvent;
import com.shop.paymentservice.repository.PaymentRepository;
import com.shop.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        paymentRepository.findByOrderId(event.getOrderId()).orElseGet(() -> {
            Payment payment = Payment.builder()
                    .orderId(event.getOrderId())
                    .userId(event.getUserId())
                    .amount(event.getAmount())
                    .currency(event.getCurrency())
                    .paymentMethod(PaymentMethod.POINTS)
                    .txnId(UUID.randomUUID().toString())
                    .paymentStatus(PaymentStatus.PENDING)
                    .build();
            return paymentRepository.save(payment);
        });
    }

    @Override
    public Optional<Payment> submitPayment(UUID orderId) {
        return paymentRepository.findByOrderId(orderId).map(payment -> {
            if (!payment.getPaymentStatus().equals(PaymentStatus.PENDING)) return payment;
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            return paymentRepository.save(payment);
        });
    }

    @Override
    public boolean refundPayment(UUID orderId) {
        return paymentRepository.findByOrderId(orderId).map(payment -> {
            if (payment.getPaymentStatus().equals(PaymentStatus.REFUNDED)) return true;
            payment.setPaymentStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
            return true;
        }).orElse(false);
    }

    @Override
    public Optional<Payment> lookupPayment(UUID orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

}
