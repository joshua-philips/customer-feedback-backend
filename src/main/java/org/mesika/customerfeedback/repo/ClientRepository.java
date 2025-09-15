package org.mesika.customerfeedback.repo;

import java.util.Optional;
import java.util.UUID;

import org.mesika.customerfeedback.models.clients.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findBySource(String source);

}
