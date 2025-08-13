package com.shop.orderservice.payload;

import lombok.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentSnapshotDto {
    private String method;
    private BigDecimal amount;
    private String currency;
    private String txnId;
    private String status;
    private String provider;
}