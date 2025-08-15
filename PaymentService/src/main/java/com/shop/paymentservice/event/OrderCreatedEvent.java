package com.shop.paymentservice.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderCreatedEvent {
    private UUID orderId;
    private UUID userId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private Instant createdAt;
}
