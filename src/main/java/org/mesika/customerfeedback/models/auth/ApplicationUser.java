package org.mesika.customerfeedback.models.auth;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.mesika.customerfeedback.models.tickets.Ticket;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "application_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ApplicationUser implements UserDetails {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    private String password;

    @Column(name = "is_account_locked")
    private Boolean accountLocked;

    @Column(name = "is_account_enabled")
    private Boolean accountEnabled;

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

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Column(name = "mfa_enabled")
    private boolean mfaEnabled;

    @Column(name = "mfa_secret")
    private String mfaSecret;

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH })
    private Set<Ticket> assignedTickets;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public void addTicket(Ticket ticket) {
        assignedTickets.add(ticket);
        ticket.setAssignedTo(this);
    }

    public void removeTicket(Ticket ticket) {
        assignedTickets.remove(ticket);
        ticket.setAssignedTo(null);
    }
}
