package org.mesika.customerfeedback.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAuthResponse {
    private boolean valid;

    @JsonProperty("customer")
    private String customerId;
    private String[] roles;

    @JsonProperty("is_admin")
    private boolean isAdmin;
}
