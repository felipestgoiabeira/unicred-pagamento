package com.unicred.controller.dto.request;

import com.unicred.validation.annotation.EnumValue;
import com.unicred.domain.PersonType;
import com.unicred.validation.annotation.CpfCnpj;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssociateRequestDTO {

    @CpfCnpj
    private String document;

    @NotBlank
    @EnumValue(enumClass = PersonType.class, message = "O tipo de pessoa deve ser PF ou PJ")
    private String personType;

    @NotBlank
    private String name;

}
