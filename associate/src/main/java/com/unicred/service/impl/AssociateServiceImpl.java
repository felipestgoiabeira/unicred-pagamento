package com.unicred.service.impl;

import com.unicred.config.AppConfiguration;
import com.unicred.domain.Associate;
import com.unicred.domain.Ticket;
import com.unicred.exception.EntityExistsException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.mapper.AssociateMapper;
import com.unicred.respository.AssociateRepository;
import com.unicred.service.AssociateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssociateServiceImpl implements AssociateService {

    private final String MESSAGE_NOT_FOUND = "Associado não encontrado";

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
    public void delete(UUID id) throws EntityNotFoundException {
        var associate = associateRepository.findById(id);

        if (associate.isEmpty()) {

            throw new EntityNotFoundException(MESSAGE_NOT_FOUND);
        }

        associateRepository.delete(associate.get());
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

