package org.mesika.customerfeedback.controllers;

import java.util.List;
import java.util.UUID;

import org.mesika.customerfeedback.dto.CustomPage;
import org.mesika.customerfeedback.dto.ticket.CommentDTO;
import org.mesika.customerfeedback.dto.ticket.TicketRequest;
import org.mesika.customerfeedback.dto.ticket.TicketResponse;
import org.mesika.customerfeedback.services.ticket.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "Tickets")
public class TicketController {
    private final TicketService ticketService;

    @PostMapping("/create/{client_id}")
    public ResponseEntity<TicketResponse> createTicket(@PathVariable("client_id") UUID clientId,
            @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.createTicket(request, clientId, null));
    }

    @GetMapping("/list")
    public ResponseEntity<CustomPage<TicketResponse>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(ticketService.getAllTickets(page, pageSize));
    }

    @GetMapping("/client/{client_id}")
    public ResponseEntity<CustomPage<TicketResponse>> getClientTickets(
            @PathVariable("client_id") UUID clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(ticketService.getClientTickets(clientId, page, pageSize, null));
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<CustomPage<TicketResponse>> getCustomerTicketsByEmail(
            @PathVariable("email") String customerEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(ticketService.getCustomerTicketsByEmail(customerEmail, page, pageSize));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<CustomPage<TicketResponse>> getUserCreatedTickets(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(ticketService.getUserCreatedTickets(username, page, pageSize));
    }

    @PutMapping("/update/{ticket_id}")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable("ticket_id") UUID ticketId,
            @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.updateTicket(ticketId, request));
    }

    @PutMapping("/assign/{ticket_id}/{user_id}")
    public ResponseEntity<TicketResponse> assignTicket(@PathVariable("ticket_id") UUID ticketId,
            @PathVariable("user_id") UUID userId) {
        return ResponseEntity.ok(ticketService.assignTicket(ticketId, userId));
    }

    @GetMapping("/{ticket_id}/comments")
    public ResponseEntity<List<CommentDTO>> getTicketComments(@PathVariable("ticket_id") UUID ticketId) {
        return ResponseEntity.ok(ticketService.getTicketComments(ticketId));
    }

    @PostMapping("/{ticket_id}/comments")
    public ResponseEntity<CommentDTO> addTicketComment(@PathVariable("ticket_id") UUID ticketId,
            @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(ticketService.addTicketComment(ticketId, commentDTO));
    }

    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<Void> removeTicketComment(@PathVariable("comment_id") UUID commentId) {
        ticketService.removeTicketComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
