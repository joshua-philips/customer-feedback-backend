package org.mesika.customerfeedback.config;

import java.io.IOException;

import org.mesika.customerfeedback.dto.client.ClientAuthRequest;
import org.mesika.customerfeedback.services.auth.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ClientApiAuthenticationProvider clientAuthProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isStaffRequest(request.getRequestURI())) {
            handleStaffAuthentication(authHeader, request);
        } else {
            handleClientAuthentication(authHeader, request, response); // Pass response parameter
        }
        filterChain.doFilter(request, response);

    }

    private void handleStaffAuthentication(String authHeader, HttpServletRequest request) {
        if (!authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("Invalid authorization format") {
            };
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }

    private void handleClientAuthentication(String authHeader, HttpServletRequest request,
            HttpServletResponse response) {

        if (!authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("Invalid authorization format") {
            };
        }

        // TODO: Get and verify from requesting url
        String clientSource = request.getRemoteAddr();
        String userToken = authHeader.substring(7);

        // Create authentication request
        ClientAuthRequest authRequest = new ClientAuthRequest();
        authRequest.setClientSource(clientSource);
        authRequest.setToken(userToken);

        Authentication authentication = clientAuthProvider.authenticate(
                new UsernamePasswordAuthenticationToken(null, authRequest));

        if (authentication != null && authentication.isAuthenticated()) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    authentication.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    private boolean isStaffRequest(String requestUri) {
        if (!requestUri.startsWith("/customer")) {
            return true;
        }

        return false;
    }

}
