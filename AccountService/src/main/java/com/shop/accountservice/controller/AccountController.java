package com.shop.accountservice.controller;


import com.shop.accountservice.payload.AccountDto;
import com.shop.accountservice.payload.CreateAccountRequest;
import com.shop.accountservice.payload.UpdateAccountRequest;
import com.shop.accountservice.service.AccountService;
import com.shop.accountservice.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * register, update, lookup
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PutMapping("/me")
    public ResponseEntity<AccountDto> updateAccount(@Valid @RequestBody UpdateAccountRequest request) {
        UUID currentUserId = jwtUtil.getCurrentUserId(); //
        return ResponseEntity.ok(accountService.updateAccount(currentUserId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }


    @GetMapping("/lookup")
    public ResponseEntity<AccountDto> getAccountByEmail(@RequestParam String email) {
        return ResponseEntity.ok(accountService.getAccountByEmail(email));
    }
}
