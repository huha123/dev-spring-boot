package dev.huha123.app.config;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import dev.huha123.app.constant.Constant;
import dev.huha123.app.entity.Member;
import dev.huha123.app.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {
    @Value("${jwt.secret}")
    private String secret;
    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /* token validate */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("JWT validate ERROR !!!");
        }
        return false;
    }

    /* token parser */
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /* create JWT token from Spring Security Authentication */
    public String createToken(Authentication authentication) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + (1000 * 60 * 2));

        Member member = (Member) authentication.getPrincipal();

        String jwtToken = Jwts.builder()
                .setId("huha123 set id")
                .setSubject("huha123 createToken 123")
                .claim(Constant.SECURITY_CLAIMS_ID.getValue(), member.getId())
                .claim(Constant.SECURITY_CLAIMS_NAME.getValue(), member.getName())
                .claim(Constant.SECURITY_CLAIMS_EMAIL.getValue(), member.getEmail())
                .claim(Constant.SECURITY_CLAIMS_AUTH.getValue(), member.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .claim(Constant.SECURITY_CLAIMS_ROLE.getValue(),
                        member.getRoles().stream().map(Role::getName).collect(Collectors.joining(",")))
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
        return jwtToken;
    }

    /* create refresh JWT token from Spring Security Authentication */
    public String createRefreshToken(Authentication authentication) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + (1000 * 60 * 60) * 24);
        String jwtToken = Jwts.builder()
                .setId("huha123 set id")
                .setSubject("huha123 refreshToken")
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
        return jwtToken;
    }

    /* token check return to Spring Security Authentication */
    public Authentication getAuthentication(String token) {
        Claims claims = this.parseClaims(token);
        log.info("### getAuthentication claims : {}", claims.toString());

        List<SimpleGrantedAuthority> authorities = Arrays
                .stream(claims.get(Constant.SECURITY_CLAIMS_AUTH.getValue()).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        log.info("claims.get(auth) :{}", claims.get(Constant.SECURITY_CLAIMS_AUTH.getValue()));
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * test 용
     */
    public String createToken(Member member) {
        long now = (new Date()).getTime();
        // Date validity = new Date(now + (1000 * 60 * 10));
        Date validity = new Date(now + (1000));

        String jwtToken = Jwts.builder()
                .setId("huha123 set id")
                .setSubject("huha123 application")
                .claim("id", member.getId())
                .claim("name", member.getName())
                .claim("password", member.getPassword())
                .claim("email", member.getEmail())
                // .claim("auth", member.getAuthorities())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
        return jwtToken;
    }

    /**
     * test 용
     */
    public String createRefreshToken(Member member) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + (1000 * 60 * 60));
        // Date validity = new Date(now + (1000 * 60));

        String jwtToken = Jwts.builder()
                .setId("huha123 set id")
                .setSubject("huha123 set subject")
                .claim("id", member.getId())
                .claim("email", member.getEmail())
                // .claim("auth", member.getAuthorities())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
        return jwtToken;
    }
}
