package com.unicred.service.impl;

import com.unicred.component.TicketComponent;
import com.unicred.domain.Associate;
import com.unicred.exception.BusinessException;
import com.unicred.exception.EntityExistsException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.exception.ExpectationFailedException;
import com.unicred.mapper.AssociateMapper;
import com.unicred.respository.AssociateRepository;
import com.unicred.service.AssociateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssociateServiceImpl implements AssociateService {

    private final String MESSAGE_NOT_FOUND = "Associado não encontrado";
    private final TicketComponent ticketComponent;

    private final AssociateRepository associateRepository;
    private final AssociateMapper associateMapper;

    @Override
    public Associate create(Associate associate) throws EntityExistsException {

        if (associateRepository.findByDocument(associate.getDocument()).isPresent())
            throw new EntityExistsException("Associado já cadastrado");

        return associateRepository.save(associate);
    }

    @Override
    public Associate findByUUID(UUID id) throws EntityNotFoundException {
        var associate = associateRepository.findById(id);

        if (associate.isEmpty()) {
            throw new EntityNotFoundException(MESSAGE_NOT_FOUND);
        }

        return associate.get();
    }

    @Override
    public Associate update(UUID id, Associate associateUpdate) throws EntityNotFoundException {
        var associateOptional = associateRepository.findById(id);

        if (associateOptional.isEmpty()) {
            throw new EntityNotFoundException(MESSAGE_NOT_FOUND);
        }

        var associate = associateOptional.get();

        associateMapper.updateAssociate(associate, associateUpdate);

        return associateRepository.save(associate);
    }

    @Override
    public void delete(UUID id) throws EntityNotFoundException, ExpectationFailedException, BusinessException {
        var associateOptional = associateRepository.findById(id);

        if (associateOptional.isEmpty()) {
            throw new EntityNotFoundException(MESSAGE_NOT_FOUND);
        }
        var associate = associateOptional.get();

        validateAwaitingPayments(associate);

        associateRepository.delete(associateOptional.get());
    }

    private void validateAwaitingPayments(Associate associate) throws
            ExpectationFailedException, BusinessException {

        var tickerAwaitingPayment = ticketComponent.getTickets(associate.getUuid()).getTickets().stream()
                .filter(ticket ->
                        ticket.getStatus().equals("AGUARDANDO_PAGAMENTO") || ticket.getStatus().equals("VENCIDO")
                )
                .findFirst();

        if (tickerAwaitingPayment.isPresent()) {
            throw new BusinessException("O associado possui boletos que ainda não foram pagos!");
        }
    }

    @Override
    public Associate findByDocument(String document) throws EntityNotFoundException {
        var associate = associateRepository.findByDocument(document);

        if (associate.isEmpty()) {
            throw new EntityNotFoundException(MESSAGE_NOT_FOUND);
        }


        return associate.get();
    }
}

