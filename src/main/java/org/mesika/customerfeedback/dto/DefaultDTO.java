package org.mesika.customerfeedback.dto;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultDTO {
    private String message;
    @Nullable
    @JsonInclude(content = Include.NON_EMPTY)
    private int status;

    public DefaultDTO(String message) {
        this.message = message;
    }

}
