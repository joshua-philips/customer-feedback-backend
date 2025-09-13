package org.mesika.customerfeedback.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class ApplicationAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(null);
    }

}
