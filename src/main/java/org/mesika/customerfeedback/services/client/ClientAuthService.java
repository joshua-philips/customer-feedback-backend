package org.mesika.customerfeedback.services.client;

import java.util.Map;

import org.mesika.customerfeedback.dto.client.ClientAuthResponse;
import org.mesika.customerfeedback.models.clients.Client;
import org.mesika.customerfeedback.models.clients.ClientIdentityProvider;
import org.mesika.customerfeedback.repo.ClientIdentityProviderRepository;
import org.mesika.customerfeedback.repo.ClientRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientAuthService {
    private final RestTemplate restTemplate;
    private final ClientRepository clientRepository;
    private final ClientIdentityProviderRepository identityProviderRepository;

    public ClientAuthResponse validateToken(String clientSource, String token) {
        Client client = clientRepository.findBySource(clientSource)
                .orElseThrow(() -> new RuntimeException("Client not found: " + clientSource));

        ClientIdentityProvider idp = identityProviderRepository.findByClientId(client.getId())
                .orElseThrow(
                        () -> new RuntimeException("Identity provider not configured for client: " + clientSource));

        return callClientAuthApi(idp, token);
    }

    private ClientAuthResponse callClientAuthApi(ClientIdentityProvider idp, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity;
        String url = idp.getVerifyUrl();

        switch (idp.getVerificationStrategy()) {
            case PARAMETER:
                url += "?" + idp.getVerifyParameter() + "=" + token;
                entity = new HttpEntity<>(headers);
                break;

            case HEADER:
                headers.set(idp.getVerifyHeader(), token);
                entity = new HttpEntity<>(headers);
                break;

            case BODY:
                Map<String, String> body = Map.of(idp.getVerifyRequestField(), token);
                entity = new HttpEntity<>(body, headers);
                break;

            default:
                throw new RuntimeException("Unsupported verification strategy: " + idp.getVerificationStrategy());
        }

        try {
            var response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return parseAuthResponse(response.getBody(), idp);
            }
        } catch (Exception e) {
            throw new RuntimeException("External authentication failed: " + e.getMessage());
        }

        return null;
    }

    private ClientAuthResponse parseAuthResponse(Map<String, Object> response, ClientIdentityProvider idp) {
        ClientAuthResponse authResponse = new ClientAuthResponse();

        String customerId = extractValueFromResponse(response, idp.getVerifyResponseCustomer());
        authResponse.setCustomerId(customerId);
        authResponse.setValid(customerId != null);

        // Extract roles if configured
        if (idp.getVerifyResponseRole() != null) {
            String roles = extractValueFromResponse(response, idp.getVerifyResponseRole());
            if (roles != null) {
                authResponse.setRoles(roles.split(","));
            }
        }

        // Check if user has admin role
        if (idp.getAdminRole() != null && authResponse.getRoles() != null) {
            authResponse.setAdmin(isAdmin(authResponse.getRoles(), idp.getAdminRole()));
        }

        return authResponse;
    }

    private String extractValueFromResponse(Map<String, Object> response, String path) {
        // Simple path resolution (e.g., "user.id" or "data.roles")
        String[] parts = path.split("\\.");
        Object current = response;

        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(part);
            } else {
                return null;
            }
        }

        return current != null ? current.toString() : null;
    }

    private boolean isAdmin(String[] roles, String adminRole) {
        for (String role : roles) {
            if (adminRole.equals(role.trim())) {
                return true;
            }
        }
        return false;
    }
}
