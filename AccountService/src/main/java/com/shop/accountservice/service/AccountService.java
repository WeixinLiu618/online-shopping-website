package com.shop.accountservice.service;


import com.shop.accountservice.payload.AccountDto;
import com.shop.accountservice.payload.CreateAccountRequest;
import com.shop.accountservice.payload.UpdateAccountRequest;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountDto createAccount(CreateAccountRequest request);

    AccountDto updateAccount(UUID accountId, UpdateAccountRequest request);

    AccountDto getAccountById(UUID accountId);

    AccountDto getAccountByEmail(String email);


}
