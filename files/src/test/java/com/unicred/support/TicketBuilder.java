package com.unicred.support;

import com.unicred.domain.Ticket;
import com.unicred.domain.TicketStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TicketBuilder {

    public static Ticket.TicketBuilder getTicketBuilder() {
        return Ticket.builder()
                .value(new BigDecimal("20.00"))
                .dueDate(LocalDate.now())
                .status(TicketStatus.AWAITING_PAYMENT);
    }
}
