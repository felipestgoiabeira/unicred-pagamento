package com.unicred.controller;

import com.unicred.controller.dto.request.PayTicketRequestDTO;
import com.unicred.controller.dto.response.TicketDTO;
import com.unicred.controller.dto.response.TicketResponseDTO;
import com.unicred.exception.BusinessException;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.exception.ExpectationFailedException;
import com.unicred.exception.model.ExceptionModel;
import com.unicred.mapper.TicketMapper;
import com.unicred.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@Tag(description = "Operações disponíveis para boleto", name = "Boleto")
@RequestMapping("/boletos")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;


    @GetMapping("{uuid_associado}")
    @Operation(summary = "Retorna os boletos do associado informado por UUID")
    public TicketResponseDTO getTickets(@PathVariable("uuid_associado") UUID uuid)
            throws ExpectationFailedException, EntityNotFoundException {
        return ticketMapper.toTicketResponseDTO(0, ticketService.getTicketsFromAssociate(uuid));
    }

    @PostMapping
    @Operation(summary = "Realize o pagamento de um boleto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Associado ou Boleto não encontrado",
                    content = @Content(schema = @Schema(implementation = ExceptionModel.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Pagamento com valor incorreto!",
                                            value = """
                                                   {
                                                        "code": 0,
                                                        "datestamp":"2023-09-01T12:14:10.831Z",
                                                        "message":"Pagamento com valor incorreto!"
                                                    }
                                                    """),
                                    @ExampleObject(
                                            name = "O boleto está vencido!",
                                            value = """
                                                   {
                                                        "code": 0,
                                                        "datestamp":"2023-09-01T12:14:10.831Z",
                                                        "message":"O boleto está vencido!"
                                                   }
                                                    """),
                                    @ExampleObject(
                                            name = "Não é possível realizar o pagamento, boleto não está aguardando pagamento",
                                            value = """
                                                    {
                                                       "code": 0,
                                                       "datestamp":"2023-09-01T12:14:10.831Z",
                                                       "message":"Não é possível realizar o pagamento, boleto não está aguardando pagamento"
                                                    }
                                                    """)
                            }, schema = @Schema(implementation = ExceptionModel.class))
            )
    }
    )
    public TicketDTO payTicket(@RequestBody @Valid PayTicketRequestDTO payTicketRequestDTO)
            throws ExpectationFailedException, EntityNotFoundException, BusinessException {

        var response = ticketService.payTicket(
                payTicketRequestDTO.getAssociateDocument(),
                ticketMapper.toTicket(payTicketRequestDTO)
        );

        return ticketMapper.toTicketResponseDTO(response);
    }

    @Operation(summary = """
                   Gere um arquivo com as dívidas AGUARDANDO PAGAMENTO do associado,
                    o arquivo gerado segue os requisitos do arquivo para pagamento em lote.
            """)
    @GetMapping(value = "/{uuid_associado}/gerar-arquivo", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> generateAndReturnFile(@PathVariable("uuid_associado") UUID uuid)
            throws ExpectationFailedException, EntityNotFoundException {


        var fileBytes = ticketService.getTicketsAwaitingPayment(uuid);
        var resource = new ByteArrayResource(fileBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "boletos-" + uuid.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileBytes.length)
                .body(resource);
    }

    @Operation(
            summary = "Realize pagamentos em lote utilizando um arquivo.",
            description = """
                    <b>Estrutura de dados do Arquivo:</b> \n
                    <i>Documento do Associado</i>: CPF/CNPJ do associado. \n
                    <i>Identificador do boleto</i>: Identificador único do boleto. \n
                    <i>Valor do pagamento</i>: Valor de Pagamento do boleto. \n
                    
                    Posição 1-14: Documento do associado - Completar com zeros à esquerda. \n
                    Posição 15-50: Identificador do boleto - UUUID gerado no serviço possui 36 caracteres. \n
                    Posição 51-70: Valor pago, sem pontuação decimal, deve ser completado com zeros à esquerda. \n
                    \n\n
                    <b>Execução Paralelizada:</b> \n
                    O processo de parse das linhas para objeto e processamento de pagamento e validação dos boletos é
                    executado de forma paralela utilizando parallelStream's da Stream API.
                    
                    """
    )
    @PostMapping(value = "/lote", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void processBatch(@RequestParam("lote") MultipartFile file) throws IOException {
        ticketService.processBatch(file);
    }

}
