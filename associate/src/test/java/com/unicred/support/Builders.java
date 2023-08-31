package com.unicred.support;

import com.unicred.domain.Associate;
import com.unicred.domain.PersonType;
import com.unicred.domain.Ticket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Builders {

    public static Associate.AssociateBuilder getAssociateBuilder() {
        return Associate.builder()
                .document("53497003085")
                .personType(PersonType.PF)
                .name("Felipe");
    }

    public static Ticket.TicketBuilder getTicketBuilder(UUID uuid) {
        return Ticket.builder()
                .value(new BigDecimal(640))
                .dueDate(LocalDate.now())
                .associateUUID(uuid);
    }
}
