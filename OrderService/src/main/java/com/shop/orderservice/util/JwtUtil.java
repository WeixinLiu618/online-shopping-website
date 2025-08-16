package com.shop.orderservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;  // Base64-encoded

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    }

    public String username(String token) {
        return parse(token).getSubject(); // email
    }

    @SuppressWarnings("unchecked")
    public List<String> roles(String token) {
        Object r = parse(token).get("roles");
        return (r instanceof List<?> l) ? (List<String>) l : Collections.emptyList();
    }

    public UUID userId(String token) {
        Object v = parse(token).get("userId");
        if (v == null) return null;
        return UUID.fromString(String.valueOf(v));
    }
}
