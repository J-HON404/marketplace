package com.unicam.cs.progettoweb.marketplace.controller;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.dto.auth.LoginRequest;
import com.unicam.cs.progettoweb.marketplace.model.enums.ProfileRole;
import com.unicam.cs.progettoweb.marketplace.model.Profile;
import com.unicam.cs.progettoweb.marketplace.security.JwtUtil;
import com.unicam.cs.progettoweb.marketplace.service.profile.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST per l'autenticazione e la registrazione degli utenti.
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ProfileService profileService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(ProfileService profileService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.profileService = profileService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
     * Effettua il login di un profilo e restituisce un token JWT.
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
        String token = jwtUtil.generateToken(
                profile.getId(),
                profile.getUsername(),
                profile.getRole().name(),
                shopId
        );
        return ResponseEntity.ok(ApiResponse.success(token));
    }


}
