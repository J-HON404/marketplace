package com.unicam.cs.progettoweb.marketplace.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Utility per la generazione, validazione e parsing dei token JWT.
 * Gestisce la creazione dei token firmati e l'estrazione dei dati dal token.
 */

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token JWT.
     * @param profileId id del profilo
     * @param username username
     * @param role ruolo utente
     * @param shopId opzionale, da passare solo se ruolo = SELLER
     * @return JWT token
     */
    public String generateToken(Long profileId, String username, String role, Long shopId) {
        var builder = Jwts.builder()
                .setSubject(username)
                .claim("profileId", profileId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration));

        // Aggiungi shopId solo se ruolo SELLER e shopId non null
        if ("SELLER".equals(role) && shopId != null) {
            builder.claim("shopId", shopId);
        }

        return builder.signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Long extractProfileId(String token) {
        return extractClaims(token).get("profileId", Long.class);
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public Long extractShopId(String token) {
        return extractClaims(token).get("shopId", Long.class);
    }

    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}

