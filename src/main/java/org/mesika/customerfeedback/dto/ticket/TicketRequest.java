package org.mesika.customerfeedback.dto.ticket;

import org.mesika.customerfeedback.models.tickets.Priority;
import org.mesika.customerfeedback.models.tickets.Status;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {

    @JsonProperty(value = "customer_name")
    private String customerName;

    @JsonProperty(value = "customer_email")
    private String customerEmail;

    @JsonProperty(value = "customer_phone")
    private String customerPhone;

    private String title;

    private String description;

    @JsonProperty(value = "module_name")
    private String module;

    private Priority priority;

    private Status status;

}
