package com.unicam.cs.progettoweb.marketplace.service.profile;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.Profile;
import com.unicam.cs.progettoweb.marketplace.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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

    public Profile findProfileByUsername(String username){
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND,"profile not found with username: " + username));
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
        if(accountRepository.existsByUsername(profile.getUsername())){
            throw new MarketplaceException(HttpStatus.CONFLICT,"profile with username "+ profile.getUsername() +" already exists");
        }
        if(accountRepository.existsByEmail(profile.getEmail())){
            throw new MarketplaceException(HttpStatus.CONFLICT,"profile with email "+ profile.getEmail() +" already exists");
        }
        return accountRepository.save(profile);
    }

    public void deleteProfile(Long profileId) {
        findProfileById(profileId);
        accountRepository.deleteById(profileId);
    }

}

