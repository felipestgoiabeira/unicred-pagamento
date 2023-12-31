package com.unicred.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boleto", indexes = @Index(name = "idx_boleto", columnList = "uuid,uuid_associado", unique = true))
public class Ticket {

    @Id
    @GeneratedValue
    private UUID uuid;

    @Column(name = "valor",  precision = 10, scale = 2, nullable = false)
    private BigDecimal value;

    @Column(name = "uuid_associado", nullable = false)
    private UUID associateUUID;

    @Column(name = "vencimento", nullable = false)
    private LocalDate dueDate;

    @Column(name = "situacao", nullable = false)
    private TicketStatus status;

    @Column(name = "documento_pagador", length = 14)
    private String payerDocument;

    @Column(name = "nome_pagador", length = 50)
    private String payerName;

    @Column(name = "nome_fantasia_pagador", length = 50)
    private String payerFantasyName;

}
