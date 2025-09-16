package org.mesika.customerfeedback.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.mesika.customerfeedback.dto.DefaultDTO;
import org.mesika.customerfeedback.dto.auth.AuthenticationResponse;
import org.mesika.customerfeedback.dto.auth.LoginRequest;
import org.mesika.customerfeedback.dto.auth.VerifyTokenRequest;
import org.mesika.customerfeedback.services.auth.AuthService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/verify-token")
    public ResponseEntity<DefaultDTO> verifyToken(
            @RequestBody VerifyTokenRequest request) {
        return ResponseEntity.ok(authService.verifyToken(request.getToken()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthException, AuthorizationDeniedException, IOException {
        response.setContentType("application/json");
        authService.refreshToken(request, response);
    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<AuthenticationResponse> passwordResetRequest(
            @RequestParam String username) throws Exception {
        return ResponseEntity.ok(authService.requestPasswordReset(username));
    }

    @PostMapping("/password-reset/{token}")
    public ResponseEntity<AuthenticationResponse> changePassWithTokenStaff(@PathVariable String token,
            @RequestBody String password)
            throws Exception {
        return ResponseEntity
                .ok(authService.changePasswordWithToken(token, password));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/totp-request")
    public void totpRequest(HttpServletRequest request, HttpServletResponse response)
            throws AuthException, WriterException {
        BufferedImage image = authService.totpRequest(request);
        if (image != null) {
            try {
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                OutputStream outputStream = response.getOutputStream();
                ImageIO.write(image, "png", outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/totp-secret")
    public ResponseEntity<String> totpRequest(HttpServletRequest request) throws AuthException {
        return ResponseEntity.ok(authService.getTotpSecret(request));
    }

    // TODO: Verify Otp For Login & Using temp jwt
    // TODO: Client Verify Token

    // TODO: Disable two setp verification
}
