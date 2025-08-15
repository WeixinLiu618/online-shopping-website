package com.shop.paymentservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class JwtUtil {

    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String base64Secret) {
        byte[] bytes = Base64.getDecoder().decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    private Jws<Claims> parse(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public String subject(String token) throws JwtException {
        return parse(token).getBody().getSubject(); // 一般是 email / username
    }

    @SuppressWarnings("unchecked")
    public List<String> roles(String token) throws JwtException {
        Object roles = parse(token).getBody().get("roles");
        if (roles instanceof Collection<?> c) {
            return c.stream().map(Object::toString).toList();
        }
        return List.of();
    }

    public UUID userId(String token) throws JwtException {
        Object v = parse(token).getBody().get("userId");
        return v == null ? null : UUID.fromString(v.toString());
    }

    public boolean isExpired(String token) {
        try {
            var exp = parse(token).getBody().getExpiration();
            return exp != null && exp.before(new java.util.Date());
        } catch (JwtException e) {
            return true;
        }
    }
}
