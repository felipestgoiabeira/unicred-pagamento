package com.unicred.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociateResponseDTO {

    private UUID uuid;

    @JsonProperty("documento")
    private String document;

    @JsonProperty("tipo_pessoa")
    private String personType;

    @JsonProperty("nome")
    private String name;

}
