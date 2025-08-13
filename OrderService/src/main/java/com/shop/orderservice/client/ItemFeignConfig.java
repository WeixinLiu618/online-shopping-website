package com.shop.orderservice.client;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class ItemFeignConfig {

    // 把调用方的 Authorization 头转发给 Item Service（方便后续全链路鉴权）
    @Bean
    public RequestInterceptor authForwardingInterceptor() {
        return template -> {
            var attrs = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
            if (attrs instanceof org.springframework.web.context.request.ServletRequestAttributes sra) {
                String auth = sra.getRequest().getHeader("Authorization");
                if (auth != null && !auth.isBlank()) template.header("Authorization", auth);
            }
        };
    }

    @Bean
    public Logger.Level feignLoggerLevel() { return Logger.Level.BASIC; }
}
