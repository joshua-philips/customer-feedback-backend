package org.mesika.customerfeedback.repo;

import java.util.Optional;
import java.util.UUID;

import org.mesika.customerfeedback.models.clients.ClientIdentityProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientIdentityProviderRepository extends JpaRepository<ClientIdentityProvider, UUID> {
    Page<ClientIdentityProvider> findAllByClient_Id(UUID clientId, Pageable pageable);

    @Query("SELECT cip FROM ClientIdentityProvider cip WHERE cip.client.source = :clientSource")
    Optional<ClientIdentityProvider> findByClientSource(@Param("clientSource") String clientSource);

    Optional<ClientIdentityProvider> findByClientId(UUID clientId);
}
