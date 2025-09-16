package org.mesika.customerfeedback.dto.auth;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUserDTO {

    @JsonProperty(access = Access.READ_ONLY)
    private UUID id;

    private String username;
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty(value = "is_account_locked", access = Access.READ_ONLY)
    private Boolean accountLocked;

    @JsonProperty(value = "is_account_enabled", access = Access.READ_ONLY)
    private Boolean accountEnabled;

    @JsonProperty(value = "created_date", access = Access.READ_ONLY)
    private Instant createdDate;

    @JsonProperty(value = "last_modified", access = Access.READ_ONLY)
    private Instant lastModified;

    @JsonProperty(value = "created_by", access = Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = Access.READ_ONLY)
    private Set<String> roles;

    @JsonProperty(value = "mfa_enabled", access = Access.READ_ONLY)
    private boolean mfaEnabled;

}
