package org.mesika.customerfeedback.controllers;

import java.util.List;
import java.util.UUID;

import org.mesika.customerfeedback.dto.CustomPage;
import org.mesika.customerfeedback.dto.auth.VerifyTokenRequest;
import org.mesika.customerfeedback.dto.client.ClientAuthResponse;
import org.mesika.customerfeedback.dto.client.ModuleDTO;
import org.mesika.customerfeedback.dto.ticket.CommentDTO;
import org.mesika.customerfeedback.dto.ticket.TicketRequest;
import org.mesika.customerfeedback.dto.ticket.TicketResponse;
import org.mesika.customerfeedback.services.auth.ClientAuthService;
import org.mesika.customerfeedback.services.client.ClientService;
import org.mesika.customerfeedback.services.ticket.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "Customer")
public class CustomerController {
    private final ClientAuthService clientAuthService;
    private final TicketService ticketService;
    private final ClientService clientService;

    @PostMapping("/verify-token")
    @PreAuthorize(value = "permitAll()")
    public ResponseEntity<ClientAuthResponse> verifyToken(
            @RequestBody VerifyTokenRequest request, HttpServletRequest servletRequest) {
        return ResponseEntity.ok(clientAuthService.validateToken(servletRequest.getRemoteHost(), request.getToken()));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/modules/list")
    public ResponseEntity<List<ModuleDTO>> listClientModules(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(clientService.listClientModules(null, httpServletRequest));

    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping("/tickets/create")
    public ResponseEntity<TicketResponse> createClientTicket(
            @RequestBody TicketRequest request, HttpServletRequest servletRequest) {
        return ResponseEntity.ok(ticketService.createTicket(request, null, servletRequest));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/tickets-overview")
    @PreAuthorize(value = "hasAuthority('Client Admin')")
    public ResponseEntity<CustomPage<TicketResponse>> listAllClientTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "20") int pageSize, HttpServletRequest servletRequest) {
        return ResponseEntity.ok(ticketService.getClientTickets(null, page, pageSize, servletRequest));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/tickets/user-created")
    public ResponseEntity<CustomPage<TicketResponse>> getUserCreatedTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(ticketService.getUserCreatedTickets(null, page, pageSize));
    }

    @SecurityRequirement(name = "Authorization")
    @PutMapping("/tickets/update/{ticket_id}")
    @PreAuthorize(value = "hasAuthority('Client Admin')")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable("ticket_id") UUID ticketId,
            @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.updateTicket(ticketId, request));
    }

    @SecurityRequirement(name = "Authorization")
    @PutMapping("/tickets/assign/{ticket_id}/{user_id}")
    @PreAuthorize(value = "hasAuthority('Client Admin')")
    public ResponseEntity<TicketResponse> assignTicket(@PathVariable("ticket_id") UUID ticketId,
            @PathVariable("user_id") UUID userId) {
        return ResponseEntity.ok(ticketService.assignTicket(ticketId, userId));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/tickets/{ticket_id}/comments")
    public ResponseEntity<List<CommentDTO>> getTicketComments(@PathVariable("ticket_id") UUID ticketId) {
        return ResponseEntity.ok(ticketService.getTicketComments(ticketId));
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping("/tickets/{ticket_id}/comments")
    public ResponseEntity<CommentDTO> addTicketComment(@PathVariable("ticket_id") UUID ticketId,
            @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(ticketService.addTicketComment(ticketId, commentDTO));
    }

}
