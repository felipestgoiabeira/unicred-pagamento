package com.unicred.controller;

import com.unicred.component.dto.AssociateResponseDTO;
import com.unicred.domain.TicketStatus;
import com.unicred.repository.TicketRepository;
import com.unicred.support.ITSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static com.unicred.support.TicketBuilder.getTicketBuilder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilesControllerTest extends ITSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private RestTemplate restTemplate;

    private static final String TICKET_API_URL = "/boletos";


    @Test
    void processBatch() throws Exception {
        var ticketBuilder = getTicketBuilder().status(TicketStatus.AWAITING_PAYMENT);


        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(ResponseEntity.ok(AssociateResponseDTO.builder().personType("PJ").build()));

        when(ticketRepository.findByUuidAndAssociateUUID(any(), any()))
                .thenReturn(Optional.of(ticketBuilder.status(TicketStatus.AWAITING_PAYMENT).build()));

        byte[] fileContent = Files.readAllBytes(Paths.get("src/test/resources/files/boletos"));
        
        var multipartFile = new MockMultipartFile(
                "lote",
                "lote",
                "text/plain",
                fileContent
        );

        mockMvc.perform(multipart(TICKET_API_URL + "/lote")
                        .file(multipartFile))
                .andExpect(status().isOk());
    }
}