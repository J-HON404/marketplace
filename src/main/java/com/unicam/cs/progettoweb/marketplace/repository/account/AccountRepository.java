package com.unicam.cs.progettoweb.marketplace.repository.account;

import com.unicam.cs.progettoweb.marketplace.model.account.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByEmail(String email);
    Optional<Profile> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
