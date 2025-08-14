package com.shop.orderservice.event;


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
    private BigDecimal totalAmount;
    private String currency;
    private Instant createdAt;
}
