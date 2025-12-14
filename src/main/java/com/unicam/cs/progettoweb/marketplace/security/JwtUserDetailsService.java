package com.unicam.cs.progettoweb.marketplace.security;

import com.unicam.cs.progettoweb.marketplace.model.profile.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.unicam.cs.progettoweb.marketplace.repository.profile.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final ProfileRepository profileRepository;

    public JwtUserDetailsService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Profile p = profileRepository.findByUsername(username).orElseThrow();

        return User.builder()
                .username(p.getUsername())
                .password(p.getPassword())
                .roles(p.getRole().name())
                .build();
    }
}
