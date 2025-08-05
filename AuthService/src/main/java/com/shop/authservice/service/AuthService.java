package com.shop.authservice.service;

import com.shop.authservice.client.AccountClient;
import com.shop.authservice.payload.AccountAuthResponse;
import com.shop.authservice.payload.LoginRequest;
import com.shop.authservice.payload.LoginResponse;
import com.shop.authservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountClient accountClient;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    public LoginResponse login(LoginRequest request) {
        AccountAuthResponse account = accountClient.getAccountForAuth(request.getEmail());

        if (account == null || !passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(account.getEmail(), account.getRoles());
        return LoginResponse.builder()
                .token(token)
                .expiresIn(System.currentTimeMillis() + 3600000)
                .build();
    }
}
