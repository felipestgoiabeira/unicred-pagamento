package com.unicred.service.impl;

import com.unicred.component.TicketComponent;
import com.unicred.domain.component.Ticket;
import com.unicred.domain.component.TicketResponse;
import com.unicred.exception.BusinessException;
import com.unicred.exception.ExpectationFailedException;
import com.unicred.mapper.AssociateMapper;
import com.unicred.domain.Associate;
import com.unicred.exception.EntityExistsException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.respository.AssociateRepository;
import com.unicred.support.TestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.unicred.support.Builders.getAssociateBuilder;
import static org.mockito.Mockito.*;

public class AssociateServiceImplTest extends TestSupport {

    @Mock
    private AssociateRepository associateRepository;

    @Mock
    private AssociateMapper associateMapper;

    @Mock
    private TicketComponent ticketComponent;

    @InjectMocks
    private AssociateServiceImpl associateService;

    @Test
    void testShouldCreateAssociate() throws EntityExistsException {

        var associateBuilder = getAssociateBuilder();

        when(associateRepository.findByDocument(anyString())).thenReturn(Optional.empty());
        when(associateRepository.save(any(Associate.class)))
                .thenReturn(associateBuilder.uuid(UUID.randomUUID()).build());

        var associate = associateService.create(associateBuilder.build());

        Assertions.assertNotNull(associate, "The associate must not be null");
    }

    @Test
    void testShouldThrowsEntityExistsExceptionWhenCreate() {

        var associateBuilder = getAssociateBuilder();

        when(associateRepository.findByDocument(anyString()))
                .thenReturn(Optional.of(associateBuilder.build()));


        Assertions.assertThrows(
                EntityExistsException.class,
                () -> associateService.create(associateBuilder.build()),
                "Assert failed, should throw EntityExistsException"
        );
    }

    @Test
    void testShouldFindByUUID() throws EntityNotFoundException {

        var uuid = UUID.randomUUID();

        var associate = getAssociateBuilder().uuid(uuid).build();

        when(associateRepository.findById(uuid)).thenReturn(Optional.of(associate));

        var associateResult = associateService.findByUUID(uuid);

        Assertions.assertNotNull(associateResult, "The associate must not be null");
    }

    @Test
    void testShouldThrowEntityNotFoundExceptionWhenFindByUUID() {

        var uuid = UUID.randomUUID();

        when(associateRepository.findById(uuid)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> associateService.findByUUID(uuid),
                "Assert failed, should throw EntityNotFoundException"
        );
    }

    @Test
    void testShouldUpdateAssociate() throws EntityNotFoundException {

        var uuid = UUID.randomUUID();

        var newName = "Felipe Goiabeira";

        var associateBuilder = getAssociateBuilder();

        Associate associateFromDatabase = associateBuilder.uuid(uuid).build();

        when(associateRepository.findById(uuid))
                .thenReturn(Optional.of(associateFromDatabase));
        when(associateRepository.save(any()))
                .thenReturn(associateBuilder.name(newName).build());

        Associate associateToUpdate = associateBuilder.build();

        var associateResult = associateService.update(uuid, associateToUpdate);

        verify(associateMapper, atLeastOnce()).updateAssociate(associateFromDatabase, associateToUpdate);

        Assertions.assertNotNull(associateResult, "The associate must not be null");
        Assertions.assertEquals(associateResult.getName(), newName, "The associate name must be null");

    }

    @Test
    void testShouldThrowEntityNotFoundExceptionWhenUpdateAssociate() {

        var uuid = UUID.randomUUID();

        var associateBuilder = getAssociateBuilder();

        when(associateRepository.findById(uuid)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> associateService.update(uuid, associateBuilder.build()),
                "Assert failed, should throw EntityNotFoundException"
        );
        
    }

    @Test
    void testShouldDeletAssociate() throws EntityNotFoundException, BusinessException, ExpectationFailedException {

        var uuid = UUID.randomUUID();

        var associate = getAssociateBuilder().uuid(uuid).build();

        TicketResponse ticketResponse = TicketResponse.builder()
                .tickets(List.of(Ticket.builder().status("PAGO").build()))
                .build();

        when(ticketComponent.getTickets(Mockito.any())).thenReturn(
                ticketResponse
        );

        when(associateRepository.findById(uuid))
                .thenReturn(Optional.of(associate));

        associateService.delete(uuid);

        verify(associateRepository, atLeastOnce()).delete(associate);

    }

    @Test
    void testShouldThrowEntityNotFoundExceptionWhenDeletAssociate() throws ExpectationFailedException {

        var uuid = UUID.randomUUID();

        TicketResponse ticketResponse = TicketResponse.builder()
                .tickets(List.of(Ticket.builder().status("PAGO").build()))
                .build();

        when(ticketComponent.getTickets(Mockito.any())).thenReturn(
                ticketResponse
        );

        when(associateRepository.findById(uuid))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> associateService.delete(uuid),
                "Assert failed, should throw EntityNotFoundException"
        );

    }

}
