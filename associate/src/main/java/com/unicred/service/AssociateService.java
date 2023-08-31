package com.unicred.service;

import com.unicred.domain.Associate;
import com.unicred.domain.Ticket;
import com.unicred.exception.EntityExistsException;
import com.unicred.exception.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

public interface AssociateService {
    Associate create(Associate associate) throws EntityExistsException;
    Associate findByUUID(UUID id) throws EntityNotFoundException;
    Associate update(UUID id, Associate associateUpdate) throws EntityNotFoundException;
    void delete(UUID id) throws EntityNotFoundException;

    Associate createTickets(UUID id, List<Ticket> tickets) throws EntityNotFoundException;
}
