package com.unicred.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketResquestDTO {

    @JsonProperty("boletos")
    @Valid
    @NotEmpty(message = "A lista de boletos não deve ser vazia")
    private List<CreateTicketDTO> tickets;

}
