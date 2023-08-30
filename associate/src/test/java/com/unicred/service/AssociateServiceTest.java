package com.unicred.service;

import com.unicred.controller.mapper.AssociateMapper;
import com.unicred.domain.Associate;
import com.unicred.domain.PersonType;
import com.unicred.exception.EntityExistsException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.respository.AssociateRepository;
import com.unicred.support.TestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

public class AssociateServiceTest extends TestSupport {

    @Mock
    private AssociateRepository associateRepository;

    @Mock
    private AssociateMapper associateMapper;

    @InjectMocks
    private AssociateService associateService;

    @Test
    void testShouldCreateAssociate() throws EntityExistsException {

        var associateBuilder = Associate.builder()
                .document("52223285031")
                .personType(PersonType.PF)
                .name("Felipe");

        Mockito.when(associateRepository.findByDocument(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(associateRepository.save(Mockito.any(Associate.class)))
                .thenReturn(associateBuilder.uuid(UUID.randomUUID()).build());

        var associate = associateService.create(associateBuilder.build());

        Assertions.assertNotNull(associate, "The associate must not be null");
    }

    @Test
    void testShouldThrowsEntityExistsExceptionWhenCreate() {

        var associateBuilder = Associate.builder()
                .document("52223285031")
                .personType(PersonType.PF)
                .name("Felipe");

        Mockito.when(associateRepository.findByDocument(Mockito.anyString()))
                .thenReturn(Optional.of(associateBuilder.build()));


        Assertions.assertThrows(
                EntityExistsException.class,
                () -> associateService.create(associateBuilder.build()),
                "Asser failed, should throw EntityExistsException"
        );
    }

    @Test
    void testShouldFindByUUID() throws EntityNotFoundException {

        var uuid = UUID.randomUUID();

        var associate = Associate.builder()
                .uuid(uuid)
                .document("52223285031")
                .personType(PersonType.PF)
                .name("Felipe")
                .build();

        Mockito.when(associateRepository.findById(uuid)).thenReturn(Optional.of(associate));

        var associateResult = associateService.findByUUID(uuid);

        Assertions.assertNotNull(associateResult, "The associate must not be null");
    }

    @Test
    void testShouldThrowEntityNotFoundExceptionWhenFindByUUID() {

        var uuid = UUID.randomUUID();

        Mockito.when(associateRepository.findById(uuid)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> associateService.findByUUID(uuid),
                "Asser failed, should throw EntityNotFoundException"
        );
    }

    @Test
    void testShouldUpdateAssociate() throws EntityNotFoundException {

        var uuid = UUID.randomUUID();

        var newName = "Felipe Goiabeira";

        var associateBuilder = Associate.builder()
                .document("52223285031")
                .personType(PersonType.PF)
                .name("Felipe");

        Associate associateFromDatabase = associateBuilder.uuid(uuid).build();

        Mockito.when(associateRepository.findById(uuid))
                .thenReturn(Optional.of(associateFromDatabase));
        Mockito.when(associateRepository.save(Mockito.any()))
                .thenReturn(associateBuilder.name(newName).build());

        Associate associateToUpdate = associateBuilder.build();

        var associateResult = associateService.update(uuid, associateToUpdate);

        Mockito.verify(associateMapper, Mockito.atLeastOnce()).updateAssociate(associateFromDatabase, associateToUpdate);

        Assertions.assertNotNull(associateResult, "The associate must not be null");
        Assertions.assertEquals(associateResult.getName(), newName, "The associate name must be null");

    }

    @Test
    void testShouldThrowEntityNotFoundExceptionWhenUpdateAssociate() {

        var uuid = UUID.randomUUID();


        var associateBuilder = Associate.builder()
                .document("52223285031")
                .personType(PersonType.PF)
                .name("Felipe");

        Mockito.when(associateRepository.findById(uuid)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> associateService.update(uuid, associateBuilder.build()),
                "Asser failed, should throw EntityNotFoundException"
        );
        
    }

    @Test
    void testShouldDeletAssociate() throws EntityNotFoundException {

        var uuid = UUID.randomUUID();

        var associate = Associate.builder()
                .uuid(uuid)
                .document("52223285031")
                .personType(PersonType.PF)
                .name("Felipe")
                .build();

        Mockito.when(associateRepository.findById(uuid))
                .thenReturn(Optional.of(associate));

        associateService.delete(uuid);

        Mockito.verify(associateRepository, Mockito.atLeastOnce()).delete(associate);

    }

    @Test
    void testShouldThrowEntityNotFoundExceptionWhenDeletAssociate() {

        var uuid = UUID.randomUUID();

        Mockito.when(associateRepository.findById(uuid))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> associateService.delete(uuid),
                "Asser failed, should throw EntityNotFoundException"
        );

    }
}
