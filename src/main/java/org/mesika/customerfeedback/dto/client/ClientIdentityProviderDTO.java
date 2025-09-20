package org.mesika.customerfeedback.dto.client;

import java.time.Instant;
import java.util.UUID;

import org.mesika.customerfeedback.models.clients.VerificationStrategy;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientIdentityProviderDTO {

    @JsonProperty(access = Access.READ_ONLY)
    private UUID id;

    // TODO: Point to client

    @NonNull
    @JsonProperty("verify_url")
    private String verifyUrl;

    @NonNull
    @JsonProperty("verification_strategy")
    private VerificationStrategy verificationStrategy;

    @JsonProperty("verify_parameter")
    private String verifyParameter;

    @JsonProperty("verify_header")
    private String verifyHeader;

    @JsonProperty("verify_request_field")
    private String verifyRequestField;

    @NonNull
    @JsonProperty("verify_response_customer")
    private String verifyResponseCustomer;

    @JsonProperty("verify_response_role")
    private String verifyResponseRole;

    @JsonProperty("admin_role")
    private String adminRole;

    @JsonProperty(value = "created_date", access = Access.READ_ONLY)
    private Instant createdDate;

    @JsonProperty(value = "last_modified", access = Access.READ_ONLY)
    private Instant lastModified;

    @JsonProperty(value = "created_by", access = Access.READ_ONLY)
    private String createdBy;
}
