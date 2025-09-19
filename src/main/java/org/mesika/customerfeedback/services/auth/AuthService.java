package org.mesika.customerfeedback.services.auth;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mesika.customerfeedback.dto.DefaultDTO;
import org.mesika.customerfeedback.dto.auth.AuthenticationResponse;
import org.mesika.customerfeedback.dto.auth.LoginRequest;
import org.mesika.customerfeedback.dto.auth.PasswordRequest;
import org.mesika.customerfeedback.models.auth.ApplicationUser;
import org.mesika.customerfeedback.models.auth.PasswordResetToken;
import org.mesika.customerfeedback.repo.ApplicationUserRepository;
import org.mesika.customerfeedback.repo.PasswordResetTokenRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;

import jakarta.mail.MessagingException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MfaService mfaService;
    private final MailService mailService;
    private final PasswordResetTokenRepository resetTokenRepository;

    public DefaultDTO verifyToken(String token) throws AuthorizationDeniedException {
        String username = jwtService.extractUsername(token);
        if (username != null) {
            ApplicationUser user = userRepository.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(token, user)) {
                return new DefaultDTO("Valid token");
            }
            throw new AuthorizationDeniedException("Invalid token");
        }
        throw new AuthorizationDeniedException("Invalid token");

    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        ApplicationUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        if (user.isMfaEnabled()) {
            boolean isCodeValid = verifyTotp(user.getUsername(), request.getCode());
            if (!isCodeValid) {
                throw new BadCredentialsException("Valid code required");
            }
        }

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .fullName(user.getFirstName() + " "
                        + " " + user.getLastName())
                .refreshToken(refreshToken)
                .roles(user.getRoles().stream()
                        .map(role -> role.getAuthority())
                        .collect(Collectors.toSet()))
                .username(user.getUsername())
                .message("Login successful")
                .mfaSetupRequired(user.getMfaSecret() == null)
                .mfaEnabled(user.isMfaEnabled())
                .build();
    }

    public AuthenticationResponse requestPasswordReset(String username) throws MessagingException {
        ApplicationUser user = userRepository.findByUsername(username).get();

        String token = createPasswordResetToken(user);
        mailService.sendResetTokenEmail("/", token, username);

        return AuthenticationResponse.builder()
                .message("Password reset request successful. Mail sent to email " + username)
                .build();
    }

    @Transactional
    public AuthenticationResponse changePasswordWithToken(String token, String password)
            throws AuthException {
        PasswordResetToken passwordResetToken = resetTokenRepository.findByToken(token).get();
        ApplicationUser user = passwordResetToken.getUser();

        if (passwordResetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new AuthException("Invalid token");
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        passwordResetToken.setExpiryDate(Instant.now());
        resetTokenRepository.delete(passwordResetToken);

        return AuthenticationResponse.builder()
                .message("Password reset successful")
                .build();

    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException, AuthException, AuthorizationDeniedException {

        ApplicationUser user = isCorrectUserFromHeaderToken(request);

        String accessToken = jwtService.generateToken(user);
        String refresh = jwtService.generateRefreshToken(user);

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .fullName(user.getFirstName() + " "
                        + " " + user.getLastName())
                .refreshToken(refresh)
                .roles(user.getRoles().stream()
                        .map(role -> role.getAuthority()).collect(Collectors.toSet()))
                .username(user.getUsername())
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);

    }

    private String createPasswordResetToken(ApplicationUser user) {
        revokePasswordResetToken(user);
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        resetTokenRepository.save(resetToken);
        return token;
    }

    private void revokePasswordResetToken(ApplicationUser user) {
        Optional<PasswordResetToken> userToken = resetTokenRepository.findByUser(user);
        if (!userToken.isPresent()) {
            return;
        }
        resetTokenRepository.deleteById(userToken.get().getId());
    }

    public DefaultDTO changePassword(PasswordRequest request, HttpServletRequest httpServletRequest)
            throws AuthException {
        ApplicationUser user = isCorrectUserFromHeaderToken(httpServletRequest);
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        if (request.getNewPassword() != request.getConfirmPassword()) {
            throw new BadCredentialsException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new DefaultDTO("Password updated successfully");
    }

    public BufferedImage totpRequest(LoginRequest request) throws AuthException, WriterException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        ApplicationUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String secret = mfaService.generateKey();

        user.setMfaEnabled(true);
        user.setMfaSecret(secret);
        userRepository.save(user);
        createPasswordResetToken(user);

        return mfaService
                .generateQRImage(secret, user.getUsername());

    }

    public String getTotpSecret(HttpServletRequest request) throws AuthException {
        ApplicationUser user = isCorrectUserFromHeaderToken(request);
        return user.getMfaSecret();
    }

    public boolean verifyTotp(String username, int code) {
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow();
        return user != null && mfaService.isValid(user.getMfaSecret(), code);
    }

    private ApplicationUser isCorrectUserFromHeaderToken(HttpServletRequest request) throws AuthException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthException("Invalid authorization header");
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username == null) {
            throw new AuthException("Invalid authorization header");

        }

        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow();

        if (!jwtService.isTokenValid(token, user)) {
            throw new AuthException("Invalid token");
        }

        return user;

    }

}
