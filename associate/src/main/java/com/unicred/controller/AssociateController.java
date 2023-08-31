package com.unicred.controller;


import com.unicred.exception.EntityNotFoundException;
import com.unicred.service.AssociateService;
import com.unicred.service.impl.AssociateServiceImpl;
import com.unicred.controller.dto.request.AssociateRequestDTO;
import com.unicred.controller.dto.response.AssociateResponseDTO;
import com.unicred.mapper.AssociateMapper;
import com.unicred.exception.EntityExistsException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@Tag(description = "Operações de CRUD para Associado", name = "Associado")
@RequestMapping("/associados")
@RequiredArgsConstructor
public class AssociateController {

    public final AssociateService associateService;
    public final AssociateMapper associateMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssociateResponseDTO create(@RequestBody @Valid AssociateRequestDTO associateRequestDTO)
            throws EntityExistsException {

        var associate = associateService.create(associateMapper.toAssociate(associateRequestDTO));

        return associateMapper.toAssociateResponseDTO(associate);
    }

    @GetMapping("{uuid}")
    public AssociateResponseDTO getAssociateByUUID(@PathVariable("uuid") UUID id) throws EntityNotFoundException {

        var associate = associateService.findByUUID(id);

        return associateMapper.toAssociateResponseDTO(associate);
    }

    @PutMapping("{uuid}")
    public AssociateResponseDTO update(@PathVariable("uuid") UUID id,
                                       @RequestBody @Valid AssociateRequestDTO associateRequestDTO)
            throws EntityNotFoundException {

        var associate = associateService.update(id,
                associateMapper.toAssociate(associateRequestDTO));

        return associateMapper.toAssociateResponseDTO(associate);
    }

    @DeleteMapping("{uuid}")
    public void delete(@PathVariable("uuid") UUID id)
            throws EntityNotFoundException {

        associateService.delete(id);
    }
}
