package com.shop.orderservice.payload;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateOrderStatusRequest {
    private String status;  // e.g. PAID / SHIPPED / CANCELLED ...
    private String reason;
}
