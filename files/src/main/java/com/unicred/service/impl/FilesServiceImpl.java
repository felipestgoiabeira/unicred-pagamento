package com.unicred.service.impl;

import com.unicred.component.AssociateComponent;
import com.unicred.domain.Ticket;
import com.unicred.domain.TicketFile;
import com.unicred.domain.TicketStatus;
import com.unicred.exception.BusinessException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.exception.ExpectationFailedException;
import com.unicred.repository.TicketRepository;
import com.unicred.service.FilesService;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesServiceImpl implements FilesService {

    private final AssociateComponent associateComponent;
    private final TicketRepository ticketRepository;

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


    @Override
    public void processBatch(MultipartFile file) throws IOException, BusinessException {

        var LINE_LENGHT = 70;

        var reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

        var exceptions = new StringBuilder();

        var tickets = reader.lines()
                .parallel()
                .filter(line -> line.length() == LINE_LENGHT)
                .map(this::parseLine)
                .collect(Collectors.toList());

        reader.close();

        tickets.parallelStream().forEach(ticket -> {
                    try {
                        var ticketToPay = Ticket.builder()
                                .uuid(ticket.getUuid())
                                .value(ticket.getValue())
                                .build();
                        payTicket(ticket.getAssociateDocument(), ticketToPay);

                    } catch (Exception e) {
                        log.error("Erro ao processar boleto", e);
                        exceptions.append("Boleto: ")
                                .append(ticket.getUuid())
                                .append(" Erro: ")
                                .append(e.getMessage())
                                .append("\n");
                    }
                }
        );

        if (!exceptions.isEmpty()) {
            throw new BusinessException(exceptions.toString());
        }
    }


    private TicketFile parseLine(String line) {

        var DOCUMENT_START = 0;
        var DOCUMENT_END_CNPJ = 14;
        var DOCUMENT_START_CPF = 3;
        var UUID_END = 50;
        var VALUE_END = 68;

        var document = line.substring(DOCUMENT_START, DOCUMENT_END_CNPJ);

        if (document.startsWith("000")) {
            document = document.substring(DOCUMENT_START_CPF, DOCUMENT_END_CNPJ);
        }

        var uuid = line.substring(DOCUMENT_END_CNPJ, UUID_END);

        var valueString = line.substring(UUID_END, VALUE_END).replaceAll("^0+(?=.)", "");

        var decimalValue = line.substring(VALUE_END);

        var value = new BigDecimal(valueString + "." + decimalValue);

        return TicketFile.builder()
                .associateDocument(document)
                .uuid(UUID.fromString(uuid))
                .value(value)
                .build();
    }


}
