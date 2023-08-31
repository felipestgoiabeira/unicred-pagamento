package com.unicred.service.impl;

import com.unicred.config.AppConfiguration;
import com.unicred.domain.Ticket;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.respository.AssociateRepository;
import com.unicred.service.AssociateTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssociateTicketServiceImpl implements AssociateTicketService {

    private final String MESSAGE_NOT_FOUND = "Associado não encontrado";

    private final AssociateRepository associateRepository;

    private final KafkaTemplate<String, Ticket> producer;

    private final AppConfiguration appConfiguration;

    @Override
    public void createTickets(UUID id, List<Ticket> tickets) throws EntityNotFoundException {
        var associateOptional = associateRepository.findById(id);

        if (associateOptional.isEmpty()) {
            throw new EntityNotFoundException(MESSAGE_NOT_FOUND);
        }

        tickets.parallelStream().forEach(ticket -> producer
                .send(appConfiguration.getTopicCreateTicket(), String.valueOf(ticket.hashCode()), ticket));

    }
}
