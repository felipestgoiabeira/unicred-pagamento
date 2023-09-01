package com.unicred.service.impl;

import com.unicred.component.AssociateComponent;
import com.unicred.component.dto.AssociateResponseDTO;
import com.unicred.domain.Ticket;
import com.unicred.domain.TicketStatus;
import com.unicred.domain.component.AssociateResponse;
import com.unicred.exception.BusinessException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.exception.ExpectationFailedException;
import com.unicred.repository.TicketRepository;
import com.unicred.support.TestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.unicred.support.TicketBuilder.getTicketBuilder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceImplTest extends TestSupport {

    @Mock
    private AssociateComponent associateComponent;
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private static final String ASSOCIATE_DOCUMENT = "02922466094";

    @Test
    void testShouldGetTicketsFromAssociate() throws ExpectationFailedException, EntityNotFoundException {
        var uuid = UUID.randomUUID();

        var associate = AssociateResponse.builder().build();

        when(associateComponent.getAssociate(any())).thenReturn(associate);

        when(ticketRepository.findByAssociateUUID(uuid)).thenReturn(List.of(Ticket.builder().build()));

        var tickets = ticketService.getTicketsFromAssociate(uuid);

        assertFalse(tickets.isEmpty(), "The list of tickets must not be empty");

    }

    @Test
    void testWhenGetTicketsFromAssociateShouldThrowNotFound() throws ExpectationFailedException, EntityNotFoundException {
        var uuid = UUID.randomUUID();

        when(associateComponent.getAssociate(any()))
                .thenThrow(new EntityNotFoundException(""));

        assertThrows(
                EntityNotFoundException.class,
                () -> ticketService.getTicketsFromAssociate(uuid),
                "Assert failed, should throw EntityNotFoundException"
        );
    }

    @Test
    void testShouldPayTicket() throws BusinessException, ExpectationFailedException, EntityNotFoundException {

        var ticketBuilder = getTicketBuilder();

        var ticket = ticketBuilder.build();

        var ticketPayment = Ticket.builder()
                .value(new BigDecimal("20.00"))
                .build();

        var associate = AssociateResponseDTO.builder().build();

        when(associateComponent.getAssociateByDocument(any())).thenReturn(associate);

        when(ticketRepository.findByUuidAndAssociateUUID(any(), any()))
                .thenReturn(Optional.of(ticket));

        when(ticketRepository.save(any()))
                .thenReturn(ticketBuilder.status(TicketStatus.PAID).build());

        var updatedTicket = ticketService.payTicket(ASSOCIATE_DOCUMENT, ticketPayment);

        assertEquals(TicketStatus.PAID, updatedTicket.getStatus(), "The status must be same");

    }

    @Test
    void testWhenPayTicketShouldNotFoundAssociate() throws ExpectationFailedException, EntityNotFoundException {

        var ticketPayment = getTicketBuilder()
                .build();

        when(associateComponent.getAssociateByDocument(any()))
                .thenThrow(new EntityNotFoundException("Associado não encontrado"));

        assertThrows(
                EntityNotFoundException.class,
                () -> ticketService.payTicket(ASSOCIATE_DOCUMENT, ticketPayment),
                "Assert failed, should throw EntityNotFoundException"
        );

    }

    @Test
    void testWhenPayTicketShouldNotFoundTicket() throws ExpectationFailedException, EntityNotFoundException {

        var ticketPayment = getTicketBuilder()
                .build();

        var associate = AssociateResponseDTO.builder().build();

        when(associateComponent.getAssociateByDocument(any())).thenReturn(associate);

        when(ticketRepository.findByUuidAndAssociateUUID(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> ticketService.payTicket(ASSOCIATE_DOCUMENT, ticketPayment),
                "Assert failed, should throw EntityNotFoundException"
        );

    }

    @Test
    void testWhenPayTicketShouldThrowErrorTicketPaid() {

        var ticket = getTicketBuilder().status(TicketStatus.PAID).build();

        var exception = assertThrows(
                BusinessException.class,
                () -> ticketService.validateTicketStatus(ticket),
                "Assert failed, should throw BusinessException"
        );

        assertEquals("Não é possível realizar o pagamento, boleto não está aguardando pagamento",
                exception.getMessage());
    }

    @Test
    void testWhenPayTicketShouldThrowErrorTicketOverdue() {

        var ticket = getTicketBuilder().dueDate(LocalDate.now().minusDays(1)).build();

        var exception = assertThrows(
                BusinessException.class,
                () -> ticketService.validateDueDate(ticket),
                "Assert failed, should throw BusinessException"
        );

        assertEquals("O boleto está vencido!", exception.getMessage());
    }

    @Test
    void testWhenPayTicketShouldThrowErrorIncorretValue() {

        var ticket = getTicketBuilder().build();

        var ticketPayment = Ticket.builder()
                .value(new BigDecimal("20.01"))
                .build();

        var exception = assertThrows(
                BusinessException.class,
                () -> ticketService.validateValue(ticket, ticketPayment),
                "Assert failed, should throw BusinessException"
        );

        assertEquals("Pagamento com valor incorreto!", exception.getMessage());
    }

    @Test
    void testProcessBatch() throws IOException, ExpectationFailedException, EntityNotFoundException, BusinessException {

        var ticketBuilder = getTicketBuilder().status(TicketStatus.AWAITING_PAYMENT);

        var associate = AssociateResponseDTO.builder().build();

        when(associateComponent.getAssociateByDocument(any())).thenReturn(associate);

        when(ticketRepository.findByUuidAndAssociateUUID(any(), any()))
                .thenReturn(Optional.of(ticketBuilder.status(TicketStatus.AWAITING_PAYMENT).build()));


        byte[] fileContent = Files.readAllBytes(Paths.get("src/test/resources/files/boletos"));


        MultipartFile multipartFile = new MockMultipartFile(
                "boletos",
                "boletos",
                "text/plain",
                fileContent
        );

        ticketService.processBatch(multipartFile);

        verify(associateComponent, times(24)).getAssociateByDocument(anyString());
    }

    @Test
    void testGetTicketsAwaitingPayment() throws ExpectationFailedException, EntityNotFoundException {

        var ticketBuilder = getTicketBuilder().status(TicketStatus.AWAITING_PAYMENT);

        var associate = AssociateResponse.builder().personType("PF").build();

        when(associateComponent.getAssociate(any())).thenReturn(associate);

        when(ticketRepository.findByAssociateUUIDAndStatus(any(), any()))
                .thenReturn(List.of(ticketBuilder.build()));

        var uuid = UUID.randomUUID();

        var result = ticketService.getTicketsAwaitingPayment(uuid);

        assertNotNull(result, "Result must not be null");

    }
}