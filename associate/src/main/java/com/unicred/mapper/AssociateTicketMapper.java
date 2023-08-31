package com.unicred.mapper;

import com.unicred.controller.dto.request.CreateTicketDTO;
import com.unicred.domain.Ticket;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AssociateTicketMapper {
    Ticket toTicket(CreateTicketDTO dto);

    default List<Ticket> toTicketList(List<CreateTicketDTO> tickets, @Context UUID uuid) {
        return tickets.stream()
                .map(this::toTicket)
                .peek(ticket -> ticket.setAssociateUUID(uuid))
                .collect(Collectors.toList());
    }
}
