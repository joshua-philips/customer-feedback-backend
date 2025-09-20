package org.mesika.customerfeedback.repo;

import java.util.UUID;

import org.mesika.customerfeedback.models.tickets.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Page<Ticket> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Page<Ticket> findByClientId(UUID clientId, Pageable pageable);

    Page<Ticket> findByCustomerEmail(String email, Pageable pageable);

    Page<Ticket> findByCreatedBy(String createdBy, Pageable pageable);
}
