package com.shop.orderservice.event;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class OrderCancelledEvent {
    private UUID orderId;
    private String reason;
    private String actor; // "user", "admin"
}