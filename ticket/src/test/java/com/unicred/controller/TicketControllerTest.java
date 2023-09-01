package com.unicred.controller;

import com.unicred.component.dto.AssociateResponseDTO;
import com.unicred.controller.dto.request.PayTicketRequestDTO;
import com.unicred.domain.TicketStatus;
import com.unicred.repository.TicketRepository;
import com.unicred.support.ITSupport;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.unicred.support.JsonConvertionUtils.asJsonString;
import static com.unicred.support.TicketBuilder.getTicketBuilder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketControllerTest extends ITSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private RestTemplate restTemplate;

    private static final String TICKET_API_URL = "/boletos";


    @Test
    void testGetTickets() throws Exception {

        var uuid = UUID.randomUUID();

        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(ResponseEntity.ok(AssociateResponseDTO.builder().build()));

        when(ticketRepository.findByAssociateUUID(Mockito.any())).thenReturn(List.of(getTicketBuilder().build()));

        mockMvc.perform(get(TICKET_API_URL + "/" + uuid)
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    void testPayTicket() throws Exception {

        var payTickerRequest = PayTicketRequestDTO.builder()
                .ticketUUID(UUID.randomUUID().toString())
                .payerName("Felipe Goiabeira")
                .associateDocument("97287006014")
                .payerDocument("97287006014")
                .value(new BigDecimal("20.00"))
                .build();

        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(ResponseEntity.ok(AssociateResponseDTO.builder().build()));

        when(ticketRepository.findByUuidAndAssociateUUID(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(getTicketBuilder().build()));

        mockMvc.perform(post(TICKET_API_URL)
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(payTickerRequest))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testGetTicketsAwaitingPayment() throws Exception {

        var uuid = UUID.randomUUID();

        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(ResponseEntity.ok(AssociateResponseDTO.builder().personType("PJ").build()));

        var ticketBuilder = getTicketBuilder().status(TicketStatus.AWAITING_PAYMENT);

        when(ticketRepository.findByAssociateUUIDAndStatus(any(), any()))
                .thenReturn(List.of(ticketBuilder.build()));

        mockMvc.perform(get(TICKET_API_URL + "/" + uuid + "/gerar-arquivo")
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

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