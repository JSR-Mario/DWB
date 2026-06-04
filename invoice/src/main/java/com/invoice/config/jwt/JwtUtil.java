package com.invoice.config.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // Misma clave que auth-service y product-service
    private final String secretKey =
            "dwb_jwt_s3cr3t_2026_X9mK4wR7nP2qZ5vL8tJ6bF0cH3gA1dY";
    private final SecretKey signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    public Claims extractClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build();

        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrae el arreglo de roles — soporta Set<String> y List<Map>
    public List<String> extractPermisos(String token) {
        Object rolesClaim = extractClaims(token).get("roles");
        if (!(rolesClaim instanceof List<?> roles)) {
            return List.of();
        }

        return roles.stream()
                .map(this::extractAuthority)
                .filter(Objects::nonNull)
                .toList();
    }

    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractClaims(token));
    }

    public Integer extractUserId(String token) {
        Object idClaim = extractClaims(token).get("id");
        if (idClaim instanceof Number number) {
            return number.intValue();
        }
        return null;
    }

    // Extrae autoridad de String o Map<String, String>
    private String extractAuthority(Object role) {
        if (role instanceof String authority) {
            return authority;
        }
        if (role instanceof java.util.Map<?, ?> roleMap) {
            Object authority = roleMap.get("authority");
            if (authority instanceof String authorityValue) {
                return authorityValue;
            }
        }
        return null;
    }
}
