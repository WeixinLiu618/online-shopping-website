package com.shop.orderservice.payload;

import lombok.*;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    private UUID userId;
    private List<OrderItemDto> items;
    private AddressDto shippingAddr;
    private AddressDto billingAddr;
    private String currency;
    private String notes;
}