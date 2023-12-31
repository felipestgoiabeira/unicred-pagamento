package com.unicred.service.impl;

import com.unicred.component.AssociateComponent;
import com.unicred.domain.Ticket;
import com.unicred.domain.TicketFile;
import com.unicred.domain.TicketStatus;
import com.unicred.exception.BusinessException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.exception.ExpectationFailedException;
import com.unicred.repository.TicketRepository;
import com.unicred.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final AssociateComponent associateComponent;
    private final TicketRepository ticketRepository;

    @Override
    public List<Ticket> getTicketsFromAssociate(UUID associateUUID)
            throws ExpectationFailedException, EntityNotFoundException {

        var associate = associateComponent.getAssociate(associateUUID);

        log.info("Returning tickets from associate: {}", associate.getUuid());

        return ticketRepository.findByAssociateUUID(associateUUID);
    }


    public Ticket payTicket(String associateDocument, Ticket payTicket)
            throws ExpectationFailedException, EntityNotFoundException, BusinessException {

        var associate = associateComponent.getAssociateByDocument(associateDocument);

        var optionalTicket = ticketRepository
                .findByUuidAndAssociateUUID(payTicket.getUuid(), associate.getUuid());

        if (optionalTicket.isEmpty()) {
            throw new EntityNotFoundException("Boleto não encontrado");
        }

        var ticket = optionalTicket.get();

        validatePayment(payTicket, ticket);

        log.info("Paying ticket {} from associate: {}", ticket.getUuid(), associate.getUuid());

        ticket.setStatus(TicketStatus.PAID);
        ticket.setPayerDocument(payTicket.getPayerDocument());
        ticket.setPayerName(payTicket.getPayerName());
        ticket.setPayerFantasyName(payTicket.getPayerFantasyName());

        return ticketRepository.save(ticket);
    }

    @Override
    public byte[] getTicketsAwaitingPayment(UUID associateUUID)
            throws ExpectationFailedException, EntityNotFoundException {

        var VALUE_LENGTH = 20;

        var associate = associateComponent.getAssociate(associateUUID);

        var tickets = ticketRepository
                .findByAssociateUUIDAndStatus(associateUUID, TicketStatus.AWAITING_PAYMENT);

        StringBuilder content = new StringBuilder();

        for (Ticket ticket : tickets) {
            var document = associate.getDocument();

            if (associate.getPersonType().equals("PF")) {
                content.append("000");
            }

            content
                    .append(document);

            content.append(ticket.getUuid());

            var value = ticket.getValue().toString().replace(".", "");

            if (value.length() < VALUE_LENGTH) {
                IntStream.range(0, VALUE_LENGTH - value.length()).forEach(i -> content.append("0"));
            }

            content.append(value)
                    .append("\n");
        }

        return content.toString().getBytes();

    }

    protected void validatePayment(Ticket payTicket, Ticket ticket) throws BusinessException {

        validateTicketStatus(ticket);

        validateDueDate(ticket);

        validateValue(payTicket, ticket);
    }

    protected void validateValue(Ticket payTicket, Ticket ticket) throws BusinessException {
        if (!payTicket.getValue().equals(ticket.getValue())) {
            throw new BusinessException("Pagamento com valor incorreto!");
        }
    }

    public void validateDueDate(Ticket ticket) throws BusinessException {
        var today = LocalDate.now();
        var dueDate = ticket.getDueDate();

        var isAbleToPay = today.isBefore(dueDate) || today.isEqual(dueDate);

        if (!isAbleToPay) {
            throw new BusinessException("O boleto está vencido!");
        }
    }

    public void validateTicketStatus(Ticket ticket) throws BusinessException {
        if (!ticket.getStatus().equals(TicketStatus.AWAITING_PAYMENT)) {
            throw new BusinessException("Não é possível realizar o pagamento, boleto não está aguardando pagamento");
        }
    }

}
