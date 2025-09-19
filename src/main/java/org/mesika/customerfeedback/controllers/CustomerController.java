package org.mesika.customerfeedback.controllers;

import org.mesika.customerfeedback.dto.auth.VerifyTokenRequest;
import org.mesika.customerfeedback.dto.client.ClientAuthResponse;
import org.mesika.customerfeedback.services.auth.ClientAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping("/verify-token")
    public ResponseEntity<ClientAuthResponse> verifyToken(
            @RequestBody VerifyTokenRequest request, HttpServletRequest servletRequest) {
        return ResponseEntity.ok(clientAuthService.validateToken(servletRequest.getRemoteHost(), request.getToken()));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/modules")
    @PreAuthorize(value = "hasAnyAuthority('Client','Client Admin')")
    public ResponseEntity<String> clientModules() {
        return ResponseEntity.ok("Accessible to all clients users");
    }

    // User's own created tickets. Use security context to get principal. Use
    // principal to get ticket by created_by from db
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/tickets")
    @PreAuthorize(value = "hasAnyAuthority('Client','Client Admin')")
    public ResponseEntity<String> listCustomerTickets() {
        return ResponseEntity.ok("Accessible to all Client Users");
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping("/tickets")
    @PreAuthorize(value = "hasAnyAuthority('Client','Client Admin')")
    public ResponseEntity<String> createTicket() {
        return ResponseEntity.ok("Accessible to all Client Users");
    }

    // All tickets for client deployment. Meant for admin users of a deployment
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/tickets-overview")
    @PreAuthorize(value = "hasAuthority('Client Admin')")
    public ResponseEntity<String> listAllCustomerTickets() {
        return ResponseEntity.ok("Accessible to Client Admin");
    }

    @SecurityRequirement(name = "Authorization")
    @PutMapping("/tickets/update/{id}")
    @PreAuthorize(value = "hasAuthority('Client Admin')")
    public ResponseEntity<String> updateTicket() {
        return ResponseEntity.ok("Accessible to Client Admin");
    }

}
