package com.shop.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter {

    private final String jwtSecret;
    private static final AntPathMatcher matcher = new AntPathMatcher();

    public JwtAuthFilter(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var req = exchange.getRequest();
        var path = req.getURI().getPath();
        var method = req.getMethod();

        // 公开路径白名单（根据你项目调整）
        if (isPublic(path, method)) {
            return chain.filter(exchange);
        }

        // 读取 Bearer Token
        var authHeader = req.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        var token = authHeader.substring(7);

        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 传递用户信息给下游（按你的 JWT 结构取字段）
        String userId = claims.get("userId", String.class);
        List<String> roles = (List<String>) claims.get("roles");

        ServerHttpRequest mutated = req.mutate()
                .header("X-User-Id", userId != null ? userId : "")
                .header("X-User-Roles", roles != null ? String.join(",", roles) : "")
                .build();

        return chain.filter(exchange.mutate().request(mutated).build());
    }

    private boolean isPublic(String path, HttpMethod method) {
        // Auth 登录注册
        if (matcher.match("/auth/**", path) || matcher.match("/api/auth/**", path)) {
            return true;
        }
        // Item 只读公开：GET /api/items/**
        if (HttpMethod.GET.equals(method) && matcher.match("/api/items/**", path)) {
            return true;
        }
        // 你也可以放开健康检查
        if (matcher.match("/actuator/health", path)) {
            return true;
        }
        return false;
    }
}

