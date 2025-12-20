package com.unicam.cs.progettoweb.marketplace.security;

import com.unicam.cs.progettoweb.marketplace.model.Profile;
import com.unicam.cs.progettoweb.marketplace.repository.ProfileRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    public CustomUserDetailsService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Profile profile = profileRepository.findByUsername(usernameOrEmail)
                .or(() -> profileRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));
        return new CustomUserDetails(profile);
    }
}

