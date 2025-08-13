package com.shop.orderservice.payload;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemDto {
    private UUID itemId;
//    private String name;
//    private BigDecimal unitPrice;
    private int quantity;
//    private String upc;
//    private String imageUrl;
}
