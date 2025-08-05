package com.shop.accountservice.controller;

import com.shop.accountservice.entity.Account;
import com.shop.accountservice.payload.AccountAuthResponse;
import com.shop.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 *  for Auth Service
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountAuthController {

    private final AccountRepository accountRepository;

    @GetMapping("/auth")
    public ResponseEntity<AccountAuthResponse> getAccountForAuth(@RequestParam String email,
            @RequestHeader(value = "X-Internal-Token", required = false) String internalToken) {

        if (!"super-secret-key".equals(internalToken)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized access");
        }
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        AccountAuthResponse response = AccountAuthResponse.builder()
                .email(account.getEmail())
                .passwordHash(account.getPasswordHash())
                .roles(account.getRoles().toArray(new String[0]))
                .build();

        return ResponseEntity.ok(response);
    }

}
