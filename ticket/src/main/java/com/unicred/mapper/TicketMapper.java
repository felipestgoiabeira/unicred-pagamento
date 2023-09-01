package com.unicred.mapper;

import com.unicred.controller.dto.request.PayTicketRequestDTO;
import com.unicred.controller.dto.response.TicketDTO;
import com.unicred.controller.dto.response.TicketResponseDTO;
import com.unicred.domain.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TicketMapper {

    Ticket toTicket(com.unicred.listener.dto.TicketDTO ticketDTO);

    @Mapping(target = "uuid", source = "ticketUUID")
    Ticket toTicket(PayTicketRequestDTO ticketRequestDTO);

    TicketDTO toTicketResponseDTO(Ticket ticket);

    List<TicketDTO> toTicketDTO(List<Ticket> tickets);

    @Mapping(target = "tickets", source = "source")
    TicketResponseDTO toTicketResponseDTO(Integer dummy, List<Ticket> source);

}
