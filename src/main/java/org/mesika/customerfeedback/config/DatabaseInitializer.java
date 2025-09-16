package org.mesika.customerfeedback.config;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.mesika.customerfeedback.models.auth.ApplicationUser;
import org.mesika.customerfeedback.models.auth.Role;
import org.mesika.customerfeedback.repo.ApplicationUserRepository;
import org.mesika.customerfeedback.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${init.init-password}")
    private String rawInitPass;

    @Value("${init.init-phone}")
    private String phone;

    @Value("${init.init-username}")
    private String username;

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    private void initializeAdminUser() throws InterruptedException, ExecutionException {

        if (roleRepository.findAll().size() < 1) {
            Role admin = Role.builder()
                    .authority("System Administrator")
                    .build();
            roleRepository.save(admin);
        }

        Optional<ApplicationUser> user = applicationUserRepository.findByUsername(username);
        if (!user.isPresent()) {
            ApplicationUser adminUser = ApplicationUser.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .password(passwordEncoder.encode(rawInitPass))
                    .roles(roleRepository.findByAuthority("System Administrator")
                            .stream().collect(Collectors.toSet()))
                    .phoneNumber(phone)
                    .username(username)
                    .email(username)
                    .build();

            applicationUserRepository.save(adminUser);
        }

    }

}
