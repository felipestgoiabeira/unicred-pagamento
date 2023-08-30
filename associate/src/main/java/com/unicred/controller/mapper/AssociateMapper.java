package com.unicred.controller.mapper;

import com.unicred.controller.dto.request.AssociateRequestDTO;
import com.unicred.controller.dto.response.AssociateResponseDTO;
import com.unicred.domain.Associate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AssociateMapper {

    Associate toAssociate(AssociateRequestDTO associateRequestDTO);

    AssociateResponseDTO toAssociateResponseDTO(Associate associate);

    @Mapping(target = "uuid", ignore = true)
    void updateAssociate(@MappingTarget Associate associate, Associate associateUpdate);
    
}
