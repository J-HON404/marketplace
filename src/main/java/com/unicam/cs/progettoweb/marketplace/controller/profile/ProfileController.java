package com.unicam.cs.progettoweb.marketplace.controller.profile;

import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.service.profile.DefaultProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final DefaultProfileService profileService;

    public ProfileController(DefaultProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{profileId}")
    public Profile getProfile(@PathVariable Long profileId) {
        return profileService.findProfileById(profileId);
    }

    @PostMapping
    public Profile createProfile(@RequestBody Profile profile) {
        return profileService.createProfile(profile);
    }

    @PutMapping("/{profileId}")
    public Profile updateProfile(
            @PathVariable Long profileId,
            @RequestBody Profile updatedProfile
    ) {
        return profileService.updateProfile(profileId, updatedProfile);
    }

    @DeleteMapping("/{profileId}")
    public void deleteProfile(@PathVariable Long profileId) {
        profileService.deleteProfile(profileId);
    }

    @GetMapping("/{profileId}/address")
    public String getProfileAddress(@PathVariable Long profileId) {
        return profileService.getProfileAddress(profileId);
    }
}
