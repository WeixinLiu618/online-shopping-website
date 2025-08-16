package com.shop.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter {

    private static final AntPathMatcher matcher = new AntPathMatcher();
    private final SecretKey key;

    // ✅ 这里按 Base64 解码后生成 HMAC 密钥，需与 Auth 服务保持一致
    public JwtAuthFilter(@Value("${jwt.secret}") String jwtSecret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var req = exchange.getRequest();
        var path = req.getURI().getPath();
        var method = req.getMethod();

        // 放行白名单（登录、公开 GET、健康检查、CORS 预检）
        if (isPublic(path, method)) {
            return chain.filter(exchange);
        }

        // 读取 Bearer Token
        var authHeader = req.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        var token = authHeader.substring(7);

        final Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key) // ✅ 使用上面构造的 HMAC-Key
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 传递用户信息给下游（按你的 JWT 结构取字段）
        String userId = claims.get("userId", String.class);

        // 兼容性处理：roles 可能是 List<?> 或字符串
        String rolesHeader = "";
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof List<?> list) {
            rolesHeader = String.join(",", list.stream().map(String::valueOf).toList());
        } else if (rolesObj instanceof String s) {
            rolesHeader = s;
        }

        ServerHttpRequest mutated = req.mutate()
                .header("X-User-Id", userId != null ? userId : "")
                .header("X-User-Roles", rolesHeader)
                .build();

        return chain.filter(exchange.mutate().request(mutated).build());
    }

    private boolean isPublic(String path, HttpMethod method) {
        if (HttpMethod.OPTIONS.equals(method)) return true;

        // login
        if (matcher.match("/auth/**", path) || matcher.match("/api/auth/**", path)) return true;

        // Account 注册
        if (HttpMethod.POST.equals(method) && matcher.match("/api/accounts/register", path)) {
            return true;
        }
        if (HttpMethod.GET.equals(method) && matcher.match("/api/items/**", path)) return true;
        if (matcher.match("/actuator/health", path)) return true;
        return false;
    }
}
