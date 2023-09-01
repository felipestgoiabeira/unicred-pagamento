package com.unicred.service;

import com.unicred.domain.Associate;
import com.unicred.exception.BusinessException;
import com.unicred.exception.EntityExistsException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.exception.ExpectationFailedException;

import java.util.UUID;

public interface AssociateService {
    Associate create(Associate associate) throws EntityExistsException;

    Associate findByUUID(UUID id) throws EntityNotFoundException;

    Associate update(UUID id, Associate associateUpdate) throws EntityNotFoundException;

    void delete(UUID id) throws EntityNotFoundException, ExpectationFailedException, BusinessException;

    Associate findByDocument(String document) throws EntityNotFoundException;
}
