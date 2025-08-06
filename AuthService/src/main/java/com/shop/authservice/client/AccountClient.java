package com.shop.authservice.client;


import com.shop.authservice.payload.AccountAuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service", url = "${account.service.url:http://localhost:8081}")
public interface AccountClient {

    @GetMapping("/api/accounts/auth")
    AccountAuthResponse getAccountForAuth(@RequestParam String email,  @RequestHeader("X-Internal-Token") String internalToken);
}
