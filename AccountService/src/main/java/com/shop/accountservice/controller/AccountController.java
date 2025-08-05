package com.shop.accountservice.controller;


import com.shop.accountservice.payload.AccountDto;
import com.shop.accountservice.payload.CreateAccountRequest;
import com.shop.accountservice.payload.UpdateAccountRequest;
import com.shop.accountservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable UUID id,
                                                    @Valid @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(accountService.updateAccount(id, request));
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
