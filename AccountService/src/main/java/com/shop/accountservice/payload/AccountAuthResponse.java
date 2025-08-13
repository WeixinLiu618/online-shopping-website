package com.shop.accountservice.payload;

import lombok.*;

import java.util.UUID;

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
    private UUID userId;
    private String status; // 使用 enum 名称，例如 "ACTIVE"  等
}