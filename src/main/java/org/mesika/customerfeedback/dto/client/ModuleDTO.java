package org.mesika.customerfeedback.dto.client;

import java.time.Instant;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDTO {

    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    // TODO: Point to client
    // @JsonProperty(value = "client_id", access = Access.READ_ONLY)
    // private UUID clientId;

    // @JsonProperty(value = "client_name", access = Access.READ_ONLY)
    // private String clientName;

    @JsonProperty(value = "client_source", access = Access.READ_ONLY)
    private String clientSource;

    @NonNull
    private String name;

    private String description;

    @JsonProperty(value = "created_date", access = Access.READ_ONLY)
    private Instant createdDate;

    @JsonProperty(value = "last_modified", access = Access.READ_ONLY)
    private Instant lastModified;

    @JsonProperty(value = "created_by", access = Access.READ_ONLY)
    private String createdBy;
}
