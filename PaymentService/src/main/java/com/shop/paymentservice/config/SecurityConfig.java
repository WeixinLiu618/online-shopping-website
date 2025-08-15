package com.shop.paymentservice.config;

import com.shop.paymentservice.security.JwtAuthenticationEntryPoint;
import com.shop.paymentservice.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 允许 @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint entryPoint;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(eh -> eh.authenticationEntryPoint(entryPoint))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 健康检查、文档等放行
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // 支付相关接口：提交支付、查询支付
                        .requestMatchers(HttpMethod.POST, "/api/payments/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payments/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // 退款、人工调整等：仅管理员
                        .requestMatchers(HttpMethod.POST, "/api/refunds/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/payments/**").hasAuthority("ROLE_ADMIN")

                        // 其他默认允许（视情况可收紧）
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
