package org.mesika.customerfeedback.repo;

import java.util.List;
import java.util.UUID;

import org.mesika.customerfeedback.models.clients.ClientIdentityProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientIdentityProviderRepository extends JpaRepository<ClientIdentityProvider, UUID> {
    List<ClientIdentityProvider> findByClient_Id(UUID clientId);
}
