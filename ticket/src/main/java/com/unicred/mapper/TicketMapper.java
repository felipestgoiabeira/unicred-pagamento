package com.unicred.mapper;

import com.unicred.controller.dto.request.PayTicketRequestDTO;
import com.unicred.controller.dto.response.TicketResponseDTO;
import com.unicred.domain.Ticket;
import com.unicred.listener.dto.TicketDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TicketMapper {

    Ticket toTicket(TicketDTO ticketDTO);

    @Mapping(target = "uuid", source = "ticketUUID")
    Ticket toTicket(PayTicketRequestDTO ticketRequestDTO);

    TicketResponseDTO toTicketResponseDTO(Ticket ticket);

    List<TicketResponseDTO> toTicketResponseDTO(List<Ticket> tickets);

}
