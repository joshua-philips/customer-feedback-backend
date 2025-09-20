package org.mesika.customerfeedback.models.clients;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.mesika.customerfeedback.models.tickets.Ticket;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Client {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String source;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = { CascadeType.MERGE, CascadeType.DETACH })
    private Set<Module> modules;

    @Column(length = 500)
    private String description;

    @OneToOne
    @JoinColumn(name = "identity_provider_id", unique = true)
    private ClientIdentityProvider identityProvider;

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

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH })
    private Set<Ticket> clientTickets;

}
