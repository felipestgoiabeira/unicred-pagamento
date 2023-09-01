package com.unicred.mapper;

import com.unicred.component.dto.AssociateResponseDTO;
import com.unicred.controller.dto.request.PayTicketRequestDTO;
import com.unicred.controller.dto.response.TicketDTO;
import com.unicred.controller.dto.response.TicketResponseDTO;
import com.unicred.domain.Ticket;
import com.unicred.domain.component.AssociateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AssociateMapper {

    AssociateResponse toAssociateResponse(AssociateResponseDTO responseDTO);

}
