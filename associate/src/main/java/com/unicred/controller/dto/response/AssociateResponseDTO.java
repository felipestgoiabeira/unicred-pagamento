package com.unicred.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class AssociateResponseDTO {

    private UUID uuid;

    @JsonProperty("documento")
    private String document;

    @JsonProperty("tipo_pessoa")
    private String personType;

    @JsonProperty("nome")
    private String name;

}
