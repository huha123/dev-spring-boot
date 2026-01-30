package dev.huha123.app.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private SecretKey key;

    @jakarta.annotation.PostConstruct
    public void init() {
        // Initialize the SecretKey using the secret
        log.info("Initializing JWT Secret Key...");
        log.info("### secret: {}", secret);
        log.info("### expiration: {}", expiration);
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        log.info("JWT Secret Key initialized.");
    }

    public String createToken(String username, String role) {
        log.info("### createToken called ###");
        Date now = new Date();
        Date validity = new Date(now.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        log.info("### validateToken called ###");
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
