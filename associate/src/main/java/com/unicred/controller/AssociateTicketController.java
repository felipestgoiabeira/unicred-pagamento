package com.unicred.controller;


import com.unicred.controller.dto.request.CreateTicketResquestDTO;
import com.unicred.controller.dto.request.GenerateTicketsResquestDTO;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.mapper.AssociateTicketMapper;
import com.unicred.service.AssociateTicketService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = """
        Valida a requisição e verifica se o usuário existe, então envia os boletos para criação através do Kafka para
        o serviço de Boletos.
    """)
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

    @PostMapping("/gerar/{uuid-associado}")
    @Operation(description = """
        O objetivo deste endpoint é gerar uma carga maior de boletos, a requisição e o associado são validados,
        com base na quantidade são gerados boletos com valores aleatórios e enviados para o serviço de boletos
        para criação. O serviço de Boletos possui um endpoint para geração do arquivo das dívidas aguardando
        pagamento do associado.
        """,
        summary = "Gere uma carga maior de boletos com valores aleatórios."
    )
    public void generate(
            @PathVariable("uuid-associado") UUID uuid,
            @RequestBody GenerateTicketsResquestDTO resquest)
            throws EntityNotFoundException {
        associateTicketService.generate(uuid, resquest.getAmount(), resquest.getDueDate());
    }
}
