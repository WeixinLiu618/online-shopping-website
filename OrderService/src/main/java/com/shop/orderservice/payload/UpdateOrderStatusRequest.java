package com.shop.orderservice.payload;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderStatusRequest {
    private String status;  // e.g. PAID / SHIPPED / CANCELLED ...
    private String reason;
}
