package com.example.CouponVoucherService.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtils {

    private static final String SECRET =
            "SuperLongHardcodedSecretKeySuperLongHardcodedSecretKeySuperLongHardcodedSecretKey1234567890";

    private final Key key;

    public JwtUtils() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }
 
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // invalid or expired token
        }
    }
 
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object userIdObj = claims.get("userId");
        if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        } else if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        } else if (userIdObj instanceof String) {
            return Long.parseLong((String) userIdObj);
        } else {
            throw new IllegalStateException("userId claim is missing or invalid");
        }
    }
}
