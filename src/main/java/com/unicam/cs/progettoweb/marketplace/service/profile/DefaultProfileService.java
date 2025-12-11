package com.unicam.cs.progettoweb.marketplace.service.profile;

import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import com.unicam.cs.progettoweb.marketplace.repository.profile.ProfileRepository;
import org.springframework.stereotype.Service;


@Service
public class DefaultProfileService implements ProfileService {

    private final ProfileRepository accountRepository;

    public DefaultProfileService(ProfileRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Profile findProfileById(Long profileId) {
        return accountRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));
    }

    @Override
    public Profile updateProfile(Long profileId, Profile updatedProfile) {
        Profile profile = findProfileById(profileId);
        profile.setUsername(updatedProfile.getUsername());
        profile.setEmail(updatedProfile.getEmail());
        profile.setPassword(updatedProfile.getPassword());
        profile.setAddress(updatedProfile.getAddress());
        return accountRepository.save(profile);
    }

    @Override
    public Profile createProfile(Profile profile) {
        return accountRepository.save(profile);
    }

    @Override
    public void deleteProfile(Long profileId) {
        if (!accountRepository.existsById(profileId)) {
            throw new RuntimeException("Profile not found with id: " + profileId);
        }
        accountRepository.deleteById(profileId);
    }

    @Override
    public String getProfileAddress(Long profileId) {
        Profile profile = findProfileById(profileId);
        return profile.getAddress();
    }
}
