package com.shop.accountservice.payload;

import lombok.*;

/**
 * used by Auth Service
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountAuthResponse {
    private String email;
    private String passwordHash;
    private String[] roles;
}