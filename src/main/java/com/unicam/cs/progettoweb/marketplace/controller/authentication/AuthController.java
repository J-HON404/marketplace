package com.unicam.cs.progettoweb.marketplace.controller.authentication;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.dto.LoginRequest;
import com.unicam.cs.progettoweb.marketplace.model.profile.Profile;
import com.unicam.cs.progettoweb.marketplace.security.JwtUtil;
import com.unicam.cs.progettoweb.marketplace.service.profile.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Profile>> register(@RequestBody Profile profile) {
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        Profile created = profileService.createProfile(profile);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request) {
        Profile profile = profileService.findProfileByUsername(request.getUsername());
        if (!passwordEncoder.matches(request.getPassword(), profile.getPassword())) {
            return ResponseEntity.status(401).body(ApiResponse.error("Invalid credentials"));
        }
        String token = jwtUtil.generateToken(profile.getId(), profile.getUsername(), profile.getRole().name());
        return ResponseEntity.ok(ApiResponse.success(token));
    }

}
