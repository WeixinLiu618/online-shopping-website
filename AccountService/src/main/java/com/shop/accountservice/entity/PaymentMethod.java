package com.shop.accountservice.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PaymentMethod {
    private String cardNumberMasked;  // **** **** **** 1234
    private String cardType;          // VISA, MasterCard
    private String expiry;            // MM/YY
}
