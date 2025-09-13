package org.mesika.customerfeedback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    @Bean
    AuditorAware<String> auditorAware() {
        return new ApplicationAuditorAware();
    }
}
