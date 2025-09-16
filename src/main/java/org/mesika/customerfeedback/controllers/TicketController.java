package org.mesika.customerfeedback.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
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
    // List Tickets - Paginated with FIlters
    // List Client Tickets - Paginated With Filters - For Client Admin
    // List Client User Tickets - Paginated With Filters - For Non Admin Client User
    // Create Ticket
    // Update Ticket - Use for Priority / Status/ Etc. Update only non-null fields
    // Delete Ticket - Restricted
}
