package org.mesika.customerfeedback.repo;

import java.util.UUID;

import org.mesika.customerfeedback.models.tickets.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
