package com.unicred.mapper;

import com.unicred.domain.Ticket;
import com.unicred.listener.dto.TicketDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TicketMapper {

    Ticket toTicket(TicketDTO ticketDTO);

}
