package org.mesika.customerfeedback.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "Authorization", scheme = "bearer")
@OpenAPIDefinition(info = @Info(title = "Customer Feedback API", version = "1.0", description = "API documentation for Customer Feedback application"))
public class SptringDocConfiguration {
}
