package org.mesika.customerfeedback.services.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
    private final Environment env;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    public void sendResetTokenEmail(
            String baseUrl, String token, String emailAddress) throws MessagingException {
        String url = baseUrl + "/auth/password-reset/" + token;
        String message = "Follow the link to reset your password\n";
        sendMessage(emailAddress, "Reset Password", message, url);
    }

    public void sendMessage(String emailAddress, String subject, String body, String url) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Context thymeleafContext = new Context();
        Map<String, Object> templateModel = new HashMap<String, Object>() {
            {
                put("title", subject);
                put("subtitle", "");
                put("body", body);
                put("url", url);
            }
        };
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("mail.html", thymeleafContext);

        helper.setFrom(env.getProperty("support.email"));
        helper.setTo(emailAddress);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        if (Arrays.asList(env.getActiveProfiles()).contains("default")) {
            System.out.printf("%s\n%s\n%s\n%s", emailAddress, subject, body, url);
            mailSender.send(mimeMessage);

        } else {
            System.out.printf("%s\n%s\n%s\n%s", emailAddress, subject, body, url);
            mailSender.send(mimeMessage);
        }
    }
}