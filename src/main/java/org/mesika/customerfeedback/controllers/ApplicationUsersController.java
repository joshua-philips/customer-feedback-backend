package org.mesika.customerfeedback.controllers;

import java.util.Set;

import org.mesika.customerfeedback.dto.CustomPage;
import org.mesika.customerfeedback.dto.DefaultDTO;
import org.mesika.customerfeedback.dto.auth.ApplicationUserDTO;
import org.mesika.customerfeedback.dto.auth.RegisterRequest;
import org.mesika.customerfeedback.dto.auth.RoleDTO;
import org.mesika.customerfeedback.exception.UsernameAlreadyExistsException;
import org.mesika.customerfeedback.services.auth.ApplicationUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "Users")
public class ApplicationUsersController {
    private final ApplicationUserService applicationUserService;

    // Update
    // Roles
    // List Users
    // Remove / Disable Users

    @PostMapping("/register")
    public ResponseEntity<DefaultDTO> register(
            @RequestBody RegisterRequest request)
            throws AuthException, UsernameAlreadyExistsException, MessagingException {
        applicationUserService.register(request);
        return ResponseEntity.ok(new DefaultDTO("User registered successfully. Credentials sent"));
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<RoleDTO>> listRoles() {
        return ResponseEntity.ok(applicationUserService.getRoles());
    }

    @GetMapping("/list")
    public ResponseEntity<CustomPage<ApplicationUserDTO>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "20") int size) {

        return ResponseEntity.ok(applicationUserService
                .getUsers(page, size));
    }

}
