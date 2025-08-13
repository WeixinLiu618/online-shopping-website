package com.shop.orderservice.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtil {
    private SecurityUtil() {}
    public static Optional<String> currentUsername() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return (a == null) ? Optional.empty() : Optional.ofNullable(a.getName());
    }
}
