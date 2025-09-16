package org.mesika.customerfeedback.services.auth;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.validator.routines.EmailValidator;
import org.mesika.customerfeedback.dto.CustomPage;
import org.mesika.customerfeedback.dto.auth.ApplicationUserDTO;
import org.mesika.customerfeedback.dto.auth.RegisterRequest;
import org.mesika.customerfeedback.dto.auth.RoleDTO;
import org.mesika.customerfeedback.exception.UsernameAlreadyExistsException;
import org.mesika.customerfeedback.models.auth.ApplicationUser;
import org.mesika.customerfeedback.models.auth.Role;
import org.mesika.customerfeedback.repo.ApplicationUserRepository;
import org.mesika.customerfeedback.repo.RoleRepository;
import org.mesika.customerfeedback.utils.RandomPasswordGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

        private final ModelMapper modelMapper;

        private final ApplicationUserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final RoleRepository roleRepository;
        private final MailService mailService;

        @Transactional
        public ApplicationUser register(RegisterRequest request)
                        throws UsernameAlreadyExistsException, MessagingException, AuthException {

                if (!EmailValidator.getInstance().isValid(request.getEmail()))
                        throw new AuthException("Invalid email");

                if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                        throw new UsernameAlreadyExistsException("Username already in use");
                }

                String password = RandomPasswordGenerator.generateRandomPassword(8);
                Set<Role> roles = Set.copyOf(roleRepository.findAllById(request.getRoleIds()));

                ApplicationUser user = ApplicationUser.builder()
                                .username(request.getUsername())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(password))
                                .roles(roles)
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .phoneNumber(request.getPhoneNumber())
                                .mfaEnabled(false)
                                .build();

                userRepository.save(user);

                mailService.sendMessage(user.getUsername(), "Authentication credentials",
                                String.format("%s\n%s\n%s", "Welcome to Customer Feedback System",
                                                user.getUsername(), password),
                                "");

                return user;
        }

        public CustomPage<ApplicationUserDTO> getUsers(int page, int size) {
                PageRequest pageRequest = PageRequest.of(page, size,
                                Sort.by("lastModified").descending()
                                                .and(Sort.by("createdDate").descending()));
                return new CustomPage<>(userRepository.findAll(pageRequest)
                                .map(user -> {
                                        ApplicationUserDTO dto = modelMapper.map(user, ApplicationUserDTO.class);
                                        dto.setRoles(user.getRoles().stream()
                                                        .map(Role::getAuthority)
                                                        .collect(Collectors.toSet()));

                                        return dto;
                                }));
        }

        public Set<RoleDTO> getRoles() {
                return roleRepository.findAll().stream()
                                .map(role -> modelMapper.map(role, RoleDTO.class))
                                .collect(Collectors.toSet());
        }
}
