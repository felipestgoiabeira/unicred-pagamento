package com.unicred.listener;

import com.unicred.domain.Ticket;
import com.unicred.listener.dto.TicketDTO;
import com.unicred.mapper.TicketMapper;
import com.unicred.repository.TicketRepository;
import com.unicred.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.kafka.support.Acknowledgment;

import java.util.UUID;

public class TicketListenerTest extends TestSupport {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketListener ticketListener;

    @Test
    void testCreateListener() {
        
        var acknowledgment = new Acknowledgment() {
            @Override
            public void acknowledge() {

            }
        };

        var ticketDTO = TicketDTO.builder()
                .associateUUID(UUID.randomUUID())
                .build();

        Mockito.when(ticketMapper.toTicket(Mockito.any(TicketDTO.class))).thenReturn(Ticket.builder().build());

        ticketListener.create(ticketDTO, acknowledgment );

        Mockito.verify(ticketRepository, Mockito.atLeastOnce()).save(Mockito.any());

    }
}
