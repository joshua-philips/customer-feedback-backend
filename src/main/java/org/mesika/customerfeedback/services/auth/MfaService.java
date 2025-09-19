package org.mesika.customerfeedback.services.auth;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MfaService {
    private final Environment env;

    public String generateKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    public boolean isValid(String secret, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator(
                new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder().build());
        return gAuth.authorize(secret, code);
    }

    public BufferedImage generateQRImage(String secret, String username) throws WriterException {
        String url = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(
                env.getProperty("spring.application.name"),
                username,
                new GoogleAuthenticatorKey.Builder(secret).build());
        return generateQRImage(url);
    }

    public static BufferedImage generateQRImage(String qrCodeText) throws WriterException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 200, 200, hintMap);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);

    }
}
