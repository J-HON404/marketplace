package com.unicam.cs.progettoweb.marketplace.controller.authentication;

import com.unicam.cs.progettoweb.marketplace.dto.*;
import com.unicam.cs.progettoweb.marketplace.model.profile.Profile;
import com.unicam.cs.progettoweb.marketplace.repository.profile.ProfileRepository;
import com.unicam.cs.progettoweb.marketplace.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, ProfileRepository profileRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (profileRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        Profile profile = new Profile();
        profile.setUsername(request.getUsername());
        profile.setEmail(request.getEmail());
        profile.setPassword(passwordEncoder.encode(request.getPassword()));
        profile.setAddress(request.getAddress());
        profile.setRole(request.getRole());
        profileRepository.save(profile);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        Profile profile = profileRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtUtil.generateToken(
                profile.getId(),
                profile.getUsername(),
                profile.getRole().name()
        );
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
