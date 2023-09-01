package com.unicred.domain.converter;

import com.unicred.domain.TicketStatus;
import com.unicred.listener.dto.TicketDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketStatusConverterTest {


    @Test
    void testConverter() {
        var ticketStatusConverter = new TicketStatusConverter();

        var resultConverterToDatabase = ticketStatusConverter.convertToDatabaseColumn(TicketStatus.PAID);

        assertEquals("PAGO", resultConverterToDatabase);

        var ticketStatus = ticketStatusConverter.convertToEntityAttribute("PAGO");

        assertEquals(TicketStatus.PAID, ticketStatus);

        var resultConverterToDatabaseNull = ticketStatusConverter.convertToDatabaseColumn(null);

        assertNull(resultConverterToDatabaseNull);

        var ticketStatusNull = ticketStatusConverter.convertToEntityAttribute(null);

        assertNull(ticketStatusNull);

    }
}