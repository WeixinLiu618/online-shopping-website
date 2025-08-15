package com.shop.paymentservice.event;
import lombok.*;

import java.math.BigDecimal;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentEvent {
    private UUID orderId;
    private BigDecimal amount;
    private String currency;
    private String method;
    private String status;
    private String txnId;
}
