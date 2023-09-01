package com.unicred.controller;

import com.unicred.component.impl.dto.TicketDTO;
import com.unicred.component.impl.dto.TicketResponseDTO;
import com.unicred.controller.dto.request.AssociateRequestDTO;
import com.unicred.domain.Associate;
import com.unicred.domain.component.Ticket;
import com.unicred.domain.component.TicketResponse;
import com.unicred.respository.AssociateRepository;
import com.unicred.support.ITSupport;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.unicred.support.Builders.*;
import static com.unicred.support.JsonConvertionUtils.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AssociateControllerTest extends ITSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssociateRepository associateRepository;

    @MockBean
    private RestTemplate restTemplate;

    private static final String ASSOCIATE_API_URL = "/associados";
    private static final String MESSAGE_NOT_FOUND = "Associado não encontrado";


    @Test
    void testShouldCreateAssociate() throws Exception {

        var associate = getAssociateBuilder().uuid(UUID.randomUUID()).build();

        var associateDTO = getAssociateRequestDTOBuilder()
                .build();

        when(associateRepository.findByDocument(anyString())).thenReturn(Optional.empty());
        when(associateRepository.save(any())).thenReturn(associate);

        mockMvc.perform(post(ASSOCIATE_API_URL)
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(associateDTO)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.uuid").value(associate.getUuid().toString()))
                .andExpect(jsonPath("$.documento").value(associate.getDocument()))
                .andExpect(jsonPath("$.tipo_pessoa").value(associate.getPersonType().toString()))
                .andExpect(jsonPath("$.nome").value(associate.getName()));
    }

    @Test
    void testShouldFailWhenCreateAssociateWithDocumentThantAlreadyExists() throws Exception {

        var associate = getAssociateBuilder().uuid(UUID.randomUUID()).build();

        var associateDTO = toAssociateRequestDTOBuilder(associate).build();

        when(associateRepository.findByDocument(anyString())).thenReturn(Optional.of(associate));
        when(associateRepository.save(any())).thenReturn(associate);


        mockMvc.perform(post(ASSOCIATE_API_URL)
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(associateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Associado já cadastrado"))
                .andDo(print());
    }

    @Test
    void testShouldGetAssociate() throws Exception {

        var associate = getAssociateBuilder().uuid(UUID.randomUUID()).build();

        when(associateRepository.findById(associate.getUuid())).thenReturn(Optional.of(associate));

        mockMvc.perform(get(ASSOCIATE_API_URL + "/" + associate.getUuid())
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.uuid").value(associate.getUuid().toString()))
                .andExpect(jsonPath("$.documento").value(associate.getDocument()))
                .andExpect(jsonPath("$.tipo_pessoa").value(associate.getPersonType().toString()))
                .andExpect(jsonPath("$.nome").value(associate.getName()));
    }

    @Test
    void testWhenGetAssociateShouldNotFound() throws Exception {

        var associate = getAssociateBuilder().uuid(UUID.randomUUID()).build();

        when(associateRepository.findById(associate.getUuid())).thenReturn(Optional.empty());

        mockMvc.perform(get(ASSOCIATE_API_URL + "/" + associate.getUuid())
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(MESSAGE_NOT_FOUND));

    }

    @Test
    void testShouldUpdateAssociate() throws Exception {

        UUID uuid = UUID.randomUUID();
        var associateBuilder = getAssociateBuilder().uuid(uuid);

        var associateDTO = getAssociateRequestDTOBuilder()
                .name("Felipe Goiabeira")
                .build();

        Associate associateUpdated = associateBuilder.name(associateDTO.getName()).build();

        when(associateRepository.findById(uuid))
                .thenReturn(Optional.of(associateBuilder.build()));

        when(associateRepository.save(any())).thenReturn(associateUpdated);

        mockMvc.perform(put(ASSOCIATE_API_URL + "/" + uuid)
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(associateDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.uuid").value(associateUpdated.getUuid().toString()))
                .andExpect(jsonPath("$.documento").value(associateUpdated.getDocument()))
                .andExpect(jsonPath("$.tipo_pessoa").value(associateUpdated.getPersonType().toString()))
                .andExpect(jsonPath("$.nome").value(associateUpdated.getName()));
    }

    @Test
    void testWhenddUpdateAssociateShouldNotFound() throws Exception {

        UUID uuid = UUID.randomUUID();
        var associateBuilder = getAssociateBuilder().uuid(uuid);

        var associateDTO = getAssociateRequestDTOBuilder()
                .name("Felipe Goiabeira")
                .build();


        when(associateRepository.findById(uuid))
                .thenReturn(Optional.empty());

        mockMvc.perform(put(ASSOCIATE_API_URL + "/" + uuid)
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(associateDTO)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(MESSAGE_NOT_FOUND));
    }

    @Test
    void testShouldDeleteAssociate() throws Exception {

        UUID uuid = UUID.randomUUID();
        var associate = getAssociateBuilder().uuid(uuid).build();

        TicketResponseDTO ticketResponse = TicketResponseDTO.builder()
                .tickets(List.of(TicketDTO.builder().status("PAGO").build()))
                .build();

        when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any())).thenReturn(
                ResponseEntity.of(Optional.of(ticketResponse))
        );

        when(associateRepository.findById(uuid))
                .thenReturn(Optional.of(associate));

        mockMvc.perform(delete(ASSOCIATE_API_URL + "/" + uuid)
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(associateRepository, atLeastOnce()).delete(associate);
    }

    @Test
    void testWhenDeleteAssociateShouldNotFound() throws Exception {

        UUID uuid = UUID.randomUUID();

        when(associateRepository.findById(uuid))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete(ASSOCIATE_API_URL + "/" + uuid)
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(associateRepository, times(0)).delete(any());
    }

    private AssociateRequestDTO.AssociateRequestDTOBuilder getAssociateRequestDTOBuilder() {
        return AssociateRequestDTO.builder()
                .name("Felipe")
                .personType("PF")
                .document("89334126035");
    }

    private AssociateRequestDTO.AssociateRequestDTOBuilder toAssociateRequestDTOBuilder(Associate associate) {
        return AssociateRequestDTO.builder()
                .name(associate.getName())
                .personType(associate.getPersonType().toString())
                .document(associate.getDocument());
    }

}
