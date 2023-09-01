package com.unicred.listener.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class TicketDTO {

    private BigDecimal value;

    private UUID associateUUID;

    private LocalDate dueDate;

}
