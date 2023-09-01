package com.unicred.domain.component;

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
public class Ticket {

    private UUID uuid;

    private BigDecimal value;

    private UUID associateUUID;

    private LocalDate dueDate;

    private String status;

    private String payerDocument;

    private String payerName;

    private String payerFantasyName;

}
