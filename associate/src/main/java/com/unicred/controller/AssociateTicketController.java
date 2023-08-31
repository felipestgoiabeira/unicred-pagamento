package com.unicred.controller;


import com.unicred.controller.dto.request.CreateTicketResquestDTO;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.mapper.AssociateMapper;
import com.unicred.mapper.AssociateTicketMapper;
import com.unicred.service.AssociateTicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@Tag(description = "Operações de boletos do associado", name = "Boletos Associado")
@RequestMapping("/associados/boletos")
@RequiredArgsConstructor
public class AssociateTicketController {

    public final AssociateTicketService associateTicketService;
    public final AssociateTicketMapper associateMapper;

    @PostMapping("{uuid-associado}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void create(
            @PathVariable("uuid-associado") UUID uuid,
            @RequestBody @Valid CreateTicketResquestDTO createTicketResquestDTO)
            throws EntityNotFoundException {

        associateTicketService.createTickets(
                uuid,
                associateMapper.toTicketList(createTicketResquestDTO.getTickets(), uuid)
        );

    }
}
