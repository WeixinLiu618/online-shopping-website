package com.shop.accountservice.payload;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodDto {
    private String cardNumberMasked;
    private String cardType;
    private String expiry;
}