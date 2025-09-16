package org.mesika.customerfeedback.repo;

import java.util.Optional;
import java.util.UUID;

import org.mesika.customerfeedback.models.auth.ApplicationUser;
import org.mesika.customerfeedback.models.auth.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(ApplicationUser user);
}
