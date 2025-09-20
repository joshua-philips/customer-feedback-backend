package org.mesika.customerfeedback.repo;

import java.util.List;
import java.util.UUID;

import org.mesika.customerfeedback.models.tickets.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByTicketIdOrderByCreatedDateAsc(UUID ticketId);

    List<Comment> findByCreatedByOrderByCreatedDateDesc(String author);

    long countByTicketId(UUID ticketId);
}
