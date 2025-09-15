package org.mesika.customerfeedback.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAuthResponse {
    private boolean valid;
    private String customerId;
    private String[] roles;
    private boolean isAdmin;
}
