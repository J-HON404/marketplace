package com.unicam.cs.progettoweb.marketplace.controller.profile;

import com.unicam.cs.progettoweb.marketplace.dto.ApiResponse;
import com.unicam.cs.progettoweb.marketplace.model.Profile;
import com.unicam.cs.progettoweb.marketplace.service.profile.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ApiResponse<Profile>> getProfile(@PathVariable Long profileId) {
        Profile profile = profileService.findProfileById(profileId);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Profile>> createProfile(@RequestBody Profile profile) {
        Profile created = profileService.createProfile(profile);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<ApiResponse<Profile>> updateProfile(@PathVariable Long profileId, @RequestBody Profile updatedProfile) {
        Profile profile = profileService.updateProfile(profileId, updatedProfile);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
    
    @DeleteMapping("/{profileId}")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@PathVariable Long profileId) {
        profileService.deleteProfile(profileId);
        return ResponseEntity.noContent().build();
    }

}
