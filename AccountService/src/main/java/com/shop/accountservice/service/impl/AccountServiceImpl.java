package com.shop.accountservice.service.impl;

import com.shop.accountservice.entity.Account;
import com.shop.accountservice.entity.AccountStatus;
import com.shop.accountservice.exception.AccountNotFoundException;
import com.shop.accountservice.exception.EmailAlreadyExistException;
import com.shop.accountservice.payload.AccountDto;
import com.shop.accountservice.payload.CreateAccountRequest;
import com.shop.accountservice.payload.UpdateAccountRequest;
import com.shop.accountservice.repository.AccountRepository;
import com.shop.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AccountDto createAccount(CreateAccountRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistException("Email already exists");
        }

        Account account = modelMapper.map(request, Account.class);
        account.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        account.setStatus(AccountStatus.ACTIVE);

        // Default role
        account.setRoles(Set.of("ROLE_USER"));

        Account saved = accountRepository.save(account);
        return modelMapper.map(saved, AccountDto.class);
    }

    @Override
    public AccountDto updateAccount(UUID accountId, UpdateAccountRequest request) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        if (request.getUsername() != null) {
            account.setUsername(request.getUsername());
        }
        if (request.getShippingAddress() != null)
            account.setShippingAddress(modelMapper.map(request.getShippingAddress(), account.getShippingAddress().getClass()));
        if (request.getBillingAddress() != null)
            account.setBillingAddress(modelMapper.map(request.getBillingAddress(), account.getBillingAddress().getClass()));
        if (request.getPaymentMethod() != null)
            account.setPaymentMethod(modelMapper.map(request.getPaymentMethod(), account.getPaymentMethod().getClass()));
        if (request.getPhoneNumber() != null) account.setPhoneNumber(request.getPhoneNumber());
        if (request.getProfileImageUrl() != null) account.setProfileImageUrl(request.getProfileImageUrl());

        Account updated = accountRepository.save(account);
        return modelMapper.map(updated, AccountDto.class);
    }

    @Override
    public AccountDto getAccountById(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return modelMapper.map(account, AccountDto.class);
    }

    @Override
    public AccountDto getAccountByEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return modelMapper.map(account, AccountDto.class);
    }


}
