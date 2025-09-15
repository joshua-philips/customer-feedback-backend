package org.mesika.customerfeedback.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientAuthRequest {
    private String clientSource;
    private String token;
}
