package org.mesika.customerfeedback.repo;

import org.mesika.customerfeedback.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
