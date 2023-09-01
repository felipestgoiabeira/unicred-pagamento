package com.unicred.component;

import com.unicred.component.dto.AssociateResponseDTO;
import com.unicred.domain.component.AssociateResponse;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.exception.ExpectationFailedException;

import java.util.UUID;

public interface AssociateComponent {
    AssociateResponse getAssociate(UUID uuid) throws
            ExpectationFailedException, EntityNotFoundException;

    AssociateResponseDTO getAssociateByDocument(String associateDocument) throws EntityNotFoundException, ExpectationFailedException;
}
