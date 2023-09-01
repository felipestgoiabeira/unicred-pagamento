package com.unicred.component.impl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {

    private UUID uuid;

    @JsonProperty("valor")
    private BigDecimal value;

    @JsonProperty("uuid_associado")
    private UUID associateUUID;

    @JsonProperty("vencimento")
    private LocalDate dueDate;

    @JsonProperty("situacao")
    private String status;

    @JsonProperty("documento_pagador")
    private String payerDocument;

    @JsonProperty("nome_pagador")
    private String payerName;

    @JsonProperty("nome_fantasia_pagador")
    private String payerFantasyName;

}
