package com.unicred.service;


import com.unicred.domain.Ticket;
import com.unicred.exception.BusinessException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.exception.ExpectationFailedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface TicketService {
    List<Ticket> getTicketsFromAssociate(UUID associateUUID)
            throws ExpectationFailedException, EntityNotFoundException;

    Ticket payTicket(String associateDocument, Ticket payTicketRequestDTO)
            throws ExpectationFailedException, EntityNotFoundException, BusinessException;

    byte[] getTicketsAwaitingPayment(UUID associateUUID)
            throws ExpectationFailedException, EntityNotFoundException;

    void processBatch(MultipartFile file) throws IOException;
}
