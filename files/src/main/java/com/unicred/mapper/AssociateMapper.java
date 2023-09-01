package com.unicred.mapper;

import com.unicred.component.dto.AssociateResponseDTO;
import com.unicred.domain.component.AssociateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AssociateMapper {

    AssociateResponse toAssociateResponse(AssociateResponseDTO responseDTO);

}
