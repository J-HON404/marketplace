package com.unicam.cs.progettoweb.marketplace.service.profile;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.repository.profile.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProfileService  {

    private final ProfileRepository accountRepository;

    public ProfileService(ProfileRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Profile findProfileById(Long profileId) {
        return accountRepository.findById(profileId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND, "profile not found with id: " + profileId));
    }

    public Profile updateProfile(Long profileId, Profile updatedProfile) {
        Profile profile = findProfileById(profileId);
        profile.setUsername(updatedProfile.getUsername());
        profile.setEmail(updatedProfile.getEmail());
        profile.setPassword(updatedProfile.getPassword());
        profile.setAddress(updatedProfile.getAddress());
        return accountRepository.save(profile);
    }

    public Profile createProfile(Profile profile) {
        return accountRepository.save(profile);
    }

    public void deleteProfile(Long profileId) {
        findProfileById(profileId);
        accountRepository.deleteById(profileId);
    }

}

