package com.shop.paymentservice.controller;

import com.shop.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/submit/{orderId}")
    public ResponseEntity<?> submitPayment(@PathVariable UUID orderId) {
        return paymentService.submitPayment(orderId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("Order not found or already paid."));
    }

    @PostMapping("/refund/{orderId}")
    public ResponseEntity<?> refund(@PathVariable UUID orderId) {
        boolean refunded = paymentService.refundPayment(orderId);
        return refunded ? ResponseEntity.ok("Refunded") : ResponseEntity.badRequest().body("Refund failed");
    }

    @GetMapping("/lookup/{orderId}")
    public ResponseEntity<?> lookup(@PathVariable UUID orderId) {
        return paymentService.lookupPayment(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



}
