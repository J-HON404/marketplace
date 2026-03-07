package com.marketplace.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Utility JWT lato API Gateway.
 * - Valida token esistenti
 * - Estrae claims principali (profileId, role, shopId)
 *
 * Non gestisce generazione dei token.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Estrae i claims dal token.
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Estrae l'ID del profilo dal token.
     */
    public Long extractProfileId(String token) {
        return extractClaims(token).get("profileId", Long.class);
    }

    /**
     * Estrae il ruolo utente dal token.
     */
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    /**
     * Estrae l'ID dello shop se presente (solo per SELLER).
     */
    public Long extractShopId(String token) {
        return extractClaims(token).get("shopId", Long.class);
    }

    /**
     * Controlla se il token è valido:
     * - firma corretta
     * - non scaduto
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return !isTokenExpired(claims);
        } catch (Exception e) {
            // Token invalido o parsing fallito
            return false;
        }
    }

    /**
     * Controlla se il token è scaduto.
     */
    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration != null && expiration.before(new Date());
    }
}