package com.unicred.listener;

import com.unicred.domain.Ticket;
import com.unicred.domain.TicketStatus;
import com.unicred.listener.dto.TicketDTO;
import com.unicred.mapper.TicketMapper;
import com.unicred.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketListener {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @KafkaListener(topics = "${topics.create-ticket}")
    public void create(@Payload TicketDTO ticketDTO, Acknowledgment acknowledgment) {
        try {
            log.info("Processing ticket {}", ticketDTO.getAssociateUUID());

            Ticket ticket = ticketMapper.toTicket(ticketDTO);
            ticket.setStatus(TicketStatus.AWAITING_PAYMENT);

            ticketRepository.save(ticket);

        } catch (Exception e) {
            log.error("Error to process ticket", e);
        } finally {
            acknowledgment.acknowledge();
        }
    }
}