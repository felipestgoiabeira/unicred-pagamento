package com.unicred.component;

import com.unicred.domain.component.TicketResponse;
import com.unicred.exception.ExpectationFailedException;

import java.util.UUID;

public interface TicketComponent {
    TicketResponse getTickets(UUID associateUUID) throws
            ExpectationFailedException;
}
