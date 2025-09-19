package org.mesika.customerfeedback.dto.auth;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String username;

    @JsonProperty("full_name")
    private String fullName;

    private Set<String> roles;
    @JsonProperty("access_token")

    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("mfa_setup_required")
    private boolean mfaSetupRequired;

    @JsonProperty("mfa_enabled")
    private boolean mfaEnabled;

    private String message;

}
