package com.unicred.controller;

import com.unicred.controller.dto.request.CreateTicketDTO;
import com.unicred.controller.dto.request.CreateTicketResquestDTO;
import com.unicred.domain.Associate;
import com.unicred.domain.Ticket;
import com.unicred.respository.AssociateRepository;
import com.unicred.support.Builders;
import com.unicred.support.ITSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.unicred.support.Builders.*;
import static com.unicred.support.JsonConvertionUtils.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AssociateTicketControllerTest extends ITSupport {

    private static final String ASSOCIATE_TICKETS_API_URL = "/associados/boletos";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssociateRepository associateRepository;

    @MockBean
    private KafkaTemplate<String, Ticket> producer;



    @Test
    void testShouldCreateAssociate() throws Exception {

        var uuid = UUID.randomUUID();

        var associate = getAssociateBuilder().uuid(uuid).build();

        var tickets = CreateTicketResquestDTO.builder()
                .tickets(List.of(
                        CreateTicketDTO.builder()
                                .value(new BigDecimal(640))
                                .dueDate(LocalDate.now())
                                .build()
                )).build();

        when(associateRepository.findById(uuid)).thenReturn(Optional.of(associate));


        mockMvc.perform(post(ASSOCIATE_TICKETS_API_URL + "/" + uuid)
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tickets)))
                .andExpect(status().isAccepted());

    }

    @Test
    void testWhenCreateTicketShouldNotFoundAssociate() throws Exception {



        var associate = getAssociateBuilder().uuid(UUID.randomUUID()).build();

        var tickets = CreateTicketResquestDTO.builder()
                .tickets(List.of(
                        CreateTicketDTO.builder()
                                .value(new BigDecimal(640))
                                .dueDate(LocalDate.now())
                                .build()
                )).build();

        when(associateRepository.findById(associate.getUuid())).thenReturn(Optional.empty());

        mockMvc.perform(post(ASSOCIATE_TICKETS_API_URL + "/" + associate.getUuid())
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tickets)))
                .andExpect(status().isNotFound());

    }

}