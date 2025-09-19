package org.mesika.customerfeedback.models.clients;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "client_identity_providers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ClientIdentityProvider {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(name = "verify_url", nullable = false)
    private String verifyUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_strategy", nullable = false, length = 20)
    private VerificationStrategy verificationStrategy;

    @Column(name = "verify_parameter", length = 100)
    private String verifyParameter;

    @Column(name = "verify_header", length = 100)
    private String verifyHeader;

    @Column(name = "verify_request_field", length = 100)
    private String verifyRequestField;

    @Column(name = "verify_response_customer", length = 100)
    private String verifyResponseCustomer;

    @Column(name = "verify_response_role", length = 100)
    private String verifyResponseRole;

    @Column(name = "admin_role", length = 100)
    private String adminRole;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified", insertable = false)
    private Instant lastModified;

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by", insertable = false, length = 100)
    private String modified_by;

    @OneToOne(mappedBy = "identityProvider")
    private Client client;
}
