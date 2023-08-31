package com.unicred.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "associado", indexes = @Index(name = "idx_documento", columnList = "documento", unique = true))
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
