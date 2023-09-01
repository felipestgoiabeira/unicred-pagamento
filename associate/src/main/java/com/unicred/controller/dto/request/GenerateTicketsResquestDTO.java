package com.unicred.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateTicketsResquestDTO {

    @JsonProperty("quantidade")
    @NotNull(message = "A quantidade deve ser informada")
    private Integer amount;

    @JsonProperty("vencimento")
    @FutureOrPresent(message = "A data de vencimento não pode ser menor que a data atual")
    private LocalDate dueDate;
}
