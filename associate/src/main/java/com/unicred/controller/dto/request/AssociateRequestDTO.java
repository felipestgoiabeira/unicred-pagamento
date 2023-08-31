package com.unicred.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unicred.validation.annotation.EnumValue;
import com.unicred.domain.PersonType;
import com.unicred.validation.annotation.CpfCnpj;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssociateRequestDTO {

    @CpfCnpj
    @JsonProperty("documento")
    private String document;

    @NotBlank
    @JsonProperty("tipo_pessoa")
    @EnumValue(enumClass = PersonType.class, message = "O valor de tipo_pessoa deve ser PF ou PJ")
    private String personType;

    @NotBlank(message = "O nome não deve ser vazio")
    @JsonProperty("nome")
    private String name;

}
