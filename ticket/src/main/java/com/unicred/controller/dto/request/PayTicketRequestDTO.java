package com.unicred.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unicred.validation.annotation.CpfCnpj;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayTicketRequestDTO {

    @JsonProperty("valor")
    @NotNull(message = "O valor de pagamento deve ser informado")
    private BigDecimal value;

    @JsonProperty("documento_associado")
    @NotBlank(message = "O documento do associado deve ser informado")
    private String associateDocument;

    @JsonProperty("uuid_boleto")
    @NotBlank(message = "O UUID do boleto deve ser informado")
    private String ticketUUID;

    @JsonProperty("documento_pagador")
    @CpfCnpj
    @NotBlank(message = "O documento do pagador deve ser informado")
    private String payerDocument;

    @JsonProperty("nome_pagador")
    @NotBlank(message = "O nome do pagador deve ser informado")
    @Length(max = 50, message = "O nome do pagador deve ter até 50 caracteres")
    private String payerName;

    @JsonProperty("nome_fantasia_pagador")
    private String payerFantasyName;

}
