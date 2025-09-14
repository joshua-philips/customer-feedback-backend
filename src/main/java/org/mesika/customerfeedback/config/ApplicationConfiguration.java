package org.mesika.customerfeedback.config;

import org.modelmapper.ModelMapper;
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

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
