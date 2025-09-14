package org.mesika.customerfeedback.repo;

import java.util.UUID;

import org.mesika.customerfeedback.models.auth.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

}
