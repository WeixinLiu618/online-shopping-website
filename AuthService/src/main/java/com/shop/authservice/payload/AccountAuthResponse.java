package com.shop.authservice.payload;

import lombok.*;

/**
 * For internal communication
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
