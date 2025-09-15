package org.mesika.customerfeedback.repo;

import java.util.Optional;
import java.util.UUID;

import org.mesika.customerfeedback.models.auth.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {
    Optional<ApplicationUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
