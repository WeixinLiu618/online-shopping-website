package com.shop.accountservice.payload;

import lombok.*;


/**
 * Response DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private String id;
    private String email;
    private String username;
    private AddressDto shippingAddress;
    private AddressDto billingAddress;
    private PaymentMethodDto paymentMethod;
    private String phoneNumber;
    private String profileImageUrl;
}

