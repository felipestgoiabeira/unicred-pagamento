package com.unicred.controller;

import com.unicred.exception.BusinessException;
import com.unicred.service.FilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@Tag(description = "Operações disponíveis para boleto", name = "Boleto")
@RequestMapping("/boletos")
@RequiredArgsConstructor
public class FilesController {

    private final FilesService ticketService;

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
    public void processBatch(@RequestParam("lote") MultipartFile file) throws IOException, BusinessException {
        ticketService.processBatch(file);
    }

}
