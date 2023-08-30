package com.unicred.service;

import com.unicred.controller.mapper.AssociateMapper;
import com.unicred.domain.Associate;
import com.unicred.exception.EntityExistsException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.respository.AssociateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssociateService {

    public final AssociateRepository associateRepository;
    public final AssociateMapper associateMapper;

    public Associate create(Associate associate) throws EntityExistsException {

        if(associateRepository.findByDocument(associate.getDocument()).isPresent())
            throw new EntityExistsException("Associado já cadastrado");

        return associateRepository.save(associate);
    }

    public Associate findByUUID(UUID id) throws EntityNotFoundException {
        var associate = associateRepository.findById(id);

        if(associate.isEmpty()){
            throw new EntityNotFoundException("Associado não encontrado");
        }

        return associate.get();
    }

    public Associate update(UUID id, Associate associateUpdate) throws EntityNotFoundException {
        var associateOptional = associateRepository.findById(id);

        if(associateOptional.isEmpty()){
            throw new EntityNotFoundException("Associado não encontrado");
        }

        var associate = associateOptional.get();

        associateMapper.updateAssociate(associate, associateUpdate);

        return associateRepository.save(associate);
    }

    public void delete(UUID id) throws EntityNotFoundException {
        var associate = associateRepository.findById(id);

        if(associate.isEmpty()){
            throw new EntityNotFoundException("Associado não encontrado");
        }

        associateRepository.delete(associate.get());
    }
}
