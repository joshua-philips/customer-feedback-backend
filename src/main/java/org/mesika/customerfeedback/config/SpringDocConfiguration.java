package org.mesika.customerfeedback.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

@Configuration
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "Authorization", scheme = "bearer")
@OpenAPIDefinition(info = @Info(title = "Customer Feedback API", version = "1.0", description = "API documentation for Customer Feedback application"), tags = {
        @Tag(name = "Auth", description = "Authentication operations"),
        @Tag(name = "Users", description = "User management (Administrative Staff Only)"),
        @Tag(name = "Clients", description = "Client deployments and registrations"),
        @Tag(name = "Tickets", description = "Administrative look at tickets from across all clients"),
        @Tag(name = "Customer", description = "Functionality available to customers under clients"),
})
public class SpringDocConfiguration {
}
