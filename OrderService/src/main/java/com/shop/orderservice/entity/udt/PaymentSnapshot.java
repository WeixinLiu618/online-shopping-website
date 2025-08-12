package com.shop.orderservice.entity.udt;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.math.BigDecimal;

@UserDefinedType("payment_snapshot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSnapshot {
    private String method;
    private BigDecimal amount;
    private String currency;
    private String txnId;
    private String status;
    private String provider;
}