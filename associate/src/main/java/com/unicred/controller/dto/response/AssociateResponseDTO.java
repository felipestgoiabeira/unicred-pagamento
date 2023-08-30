package com.unicred.controller.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class AssociateResponseDTO {

    private UUID uuid;

    private String document;

    private String personType;

    private String name;

}
