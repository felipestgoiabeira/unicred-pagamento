package com.unicred.service.impl;

import com.unicred.config.AppConfiguration;
import com.unicred.domain.Ticket;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.respository.AssociateRepository;
import com.unicred.support.TestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.unicred.support.Builders.getAssociateBuilder;
import static com.unicred.support.Builders.getTicketBuilder;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.when;

class AssociateTicketServiceImplTest extends TestSupport {

    @Mock
    private AssociateRepository associateRepository;

    @Mock
    private KafkaTemplate<String, Ticket> producer;

    @Mock
    private AppConfiguration appConfiguration;

    @InjectMocks
    private AssociateTicketServiceImpl associateTicketService;

    @Test
    void testShouldCreateTickets() throws EntityNotFoundException {

        var uuid = UUID.randomUUID();

        var associate = getAssociateBuilder().uuid(uuid).build();

        var ticket = getTicketBuilder(uuid)
                .build();

        var tickets = List.of(
                ticket
        );

        when(associateRepository.findById(uuid)).thenReturn(Optional.of(associate));
        when(appConfiguration.getTopicCreateTicket()).thenReturn("create-ticket");


        associateTicketService.createTickets(uuid, tickets);

        Mockito.verify(producer, atLeastOnce())
                .send(Mockito.anyString(), Mockito.anyString(), Mockito.any(Ticket.class));
    }

    @Test
    void testShouldThrowEntityNotFoundExceptionWhenCreateTickets() {

        var uuid = UUID.randomUUID();

        var ticket = getTicketBuilder(uuid)
                .build();

        var tickets = List.of(
                ticket
        );

        when(associateRepository.findById(uuid))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> associateTicketService.createTickets(uuid, tickets),
                "Asser failed, should throw EntityNotFoundException"
        );

    }

}