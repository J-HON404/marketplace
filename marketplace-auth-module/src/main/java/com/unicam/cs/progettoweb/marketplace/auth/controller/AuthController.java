package com.unicam.cs.progettoweb.marketplace.auth.controller;

import com.unicam.cs.progettoweb.marketplace.auth.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.auth.dto.auth.LoginRequest;
import com.unicam.cs.progettoweb.marketplace.auth.model.Profile;
import com.unicam.cs.progettoweb.marketplace.auth.model.enums.ProfileRole;
import com.unicam.cs.progettoweb.marketplace.auth.security.JwtUtil;
import com.unicam.cs.progettoweb.marketplace.auth.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Controller REST per l'autenticazione e la registrazione degli utenti.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ProfileService profileService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate; // Redis per caching token

    public AuthController(ProfileService profileService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, StringRedisTemplate redisTemplate) {
        this.profileService = profileService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Registra un nuovo profilo seller o customer
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Profile>> register(@RequestBody Profile profile) {
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));

        if (profile.getRole() == null) {
            profile.setRole(ProfileRole.CUSTOMER);
        } else {
            try {
                profile.setRole(ProfileRole.valueOf(profile.getRole().name().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid role: " + profile.getRole()));
            }
        }

        Profile created = profileService.createProfile(profile);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    /**
     * Effettua il login di un profilo, restituisce un token JWT e lo salva in Redis.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request) {
        Profile profile = profileService.findProfileByUsername(request.getUsername());

        if (profile == null || !passwordEncoder.matches(request.getPassword(), profile.getPassword())) {
            return ResponseEntity.status(401).body(ApiResponse.error("Invalid credentials"));
        }

        Long shopId = null;
        if (profile.getRole() == ProfileRole.SELLER && profile.getShop() != null) {
            shopId = profile.getShop().getId();
        }

        // Genera il token JWT
        String token = jwtUtil.generateToken(
                profile.getId(),
                profile.getUsername(),
                profile.getRole().name(),
                shopId
        );

        // Salva il token in Redis (metodo separato)
        saveTokenInRedis(token, profile.getId(), profile.getRole(), shopId);

        return ResponseEntity.ok(ApiResponse.success(token));
    }

    /**
     * Metodo privato per salvare il token e i dati utente in Redis con scadenza di 24 ore.
     */
    private void saveTokenInRedis(String token, Long profileId, ProfileRole role, Long shopId) {
        String cacheValue = profileId + "|" + role.name() + "|" + (shopId != null ? shopId : "");
        try {
            redisTemplate.opsForValue().set("auth:token:" + token, cacheValue, 24, TimeUnit.HOURS);
        } catch (Exception e) {
            System.err.println("Errore salvataggio Redis: " + e.getMessage());
        }
    }
}