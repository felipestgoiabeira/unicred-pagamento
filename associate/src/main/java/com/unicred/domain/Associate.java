package com.unicred.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "associado")
public class Associate {

    @Id
    @GeneratedValue
    private UUID uuid;

    @Column(name = "documento", length = 14, nullable = false)
    private String document;

    @Column(name = "tipo_pessoa", length = 2, nullable = false)
    @Enumerated(EnumType.STRING)
    private PersonType personType;

    @Column(name = "nome", length = 50, nullable = false)
    private String name;
}
