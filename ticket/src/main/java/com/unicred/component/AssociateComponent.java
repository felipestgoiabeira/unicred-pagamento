package com.unicred.component;

import com.unicred.component.dto.AssociateResponseDTO;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.exception.ExpectationFailedException;

import java.util.UUID;

public interface AssociateComponent {
    AssociateResponseDTO getAssociate(UUID uuid) throws
            ExpectationFailedException, EntityNotFoundException;

    AssociateResponseDTO getAssociateByDocument(String associateDocument) throws EntityNotFoundException, ExpectationFailedException;
}
