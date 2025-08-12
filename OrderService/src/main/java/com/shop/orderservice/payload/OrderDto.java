package com.shop.orderservice.payload;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private UUID orderId;
    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private String status;
    private BigDecimal totalAmount;
    private String currency;
    private AddressDto shippingAddr;
    private AddressDto billingAddr;
    private List<OrderItemDto> items;
    private PaymentSnapshotDto payment;
    private String notes;
}
