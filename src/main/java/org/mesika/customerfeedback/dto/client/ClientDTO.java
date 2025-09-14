package org.mesika.customerfeedback.dto.client;

import java.time.Instant;
import java.util.UUID;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    @JsonProperty(access = Access.READ_ONLY)
    private UUID id;

    @NonNull
    @JsonProperty(required = true)
    private String source;

    @JsonProperty(required = true)
    private String name;

    private String description;

    @JsonProperty(value = "created_date", access = Access.READ_ONLY)
    private Instant createdDate;

    @JsonProperty(value = "last_modified", access = Access.READ_ONLY)
    private Instant lastModified;

    @JsonProperty(value = "created_by", access = Access.READ_ONLY)
    private String createdBy;

}
