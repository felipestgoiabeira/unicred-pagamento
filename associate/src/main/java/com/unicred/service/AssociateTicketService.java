package com.unicred.service;

import com.unicred.domain.Ticket;
import com.unicred.exception.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AssociateTicketService {

    void createTickets(UUID id, List<Ticket> tickets) throws EntityNotFoundException;

    void generate(UUID id, Integer amount, LocalDate dueDate) throws EntityNotFoundException;
}
