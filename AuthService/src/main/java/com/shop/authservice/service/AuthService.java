package com.shop.authservice.service;

import com.shop.authservice.client.AccountClient;
import com.shop.authservice.exception.InvalidCredentialsException;
import com.shop.authservice.payload.AccountAuthResponse;
import com.shop.authservice.payload.LoginRequest;
import com.shop.authservice.payload.LoginResponse;
import com.shop.authservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountClient accountClient;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private static final java.util.Set<String> LOGIN_ALLOWED = java.util.Set.of("ACTIVE");


    public LoginResponse login(LoginRequest request) {

        // 1) 查账户（Feign 自动加 X-Internal-Token）
        AccountAuthResponse account = accountClient.getAccountForAuth(request.getEmail());

        // 2) 基础校验：存在 + 密码
        if (account == null || !passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // 3) 状态校验（只允许 ACTIVE）
        if (account.getStatus() == null || !LOGIN_ALLOWED.contains(account.getStatus())) {
            // 也可以抛 DisabledException/Forbidden 并由全局异常映射为 403
            throw new InvalidCredentialsException("Account status not allowed: " + account.getStatus());
        }

        // 4) 生成 JWT（roles 判空、防 NPE）
        String[] roles = account.getRoles() == null ? new String[0] : account.getRoles();

        String token = jwtUtil.generateToken(
                account.getEmail(),
                roles,
                account.getUserId()
        );

        long expiresAt = System.currentTimeMillis() + 3600000L; // 1 hour

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expiresAt)
                .build();
    }
}
