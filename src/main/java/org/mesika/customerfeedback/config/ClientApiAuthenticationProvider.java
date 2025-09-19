package org.mesika.customerfeedback.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mesika.customerfeedback.dto.client.ClientAuthRequest;
import org.mesika.customerfeedback.dto.client.ClientAuthResponse;
import org.mesika.customerfeedback.services.auth.ClientAuthService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClientApiAuthenticationProvider implements AuthenticationProvider {

    private final ClientAuthService clientAuthService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ClientAuthRequest authRequest = (ClientAuthRequest) authentication.getCredentials();

        try {
            ClientAuthResponse authResponse = clientAuthService.validateToken(
                    authRequest.getClientSource(),
                    authRequest.getToken());

            if (authResponse != null && authResponse.isValid()) {
                // Build authorities
                List<SimpleGrantedAuthority> authorities = Arrays.stream(authResponse.getRoles())
                        .map(role -> new SimpleGrantedAuthority(role.trim()))
                        .collect(Collectors.toList());
                authorities.add(new SimpleGrantedAuthority("Client"));

                if (authResponse.isAdmin()) {
                    authorities.add(new SimpleGrantedAuthority("Client Admin"));
                }

                return new UsernamePasswordAuthenticationToken(
                        authResponse.getCustomerId(),
                        null,
                        authorities);
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Client authentication failed: " + e.getMessage());
        }

        throw new BadCredentialsException("Client authentication failed");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClientAuthRequest.class.isAssignableFrom(authentication);
    }
}
