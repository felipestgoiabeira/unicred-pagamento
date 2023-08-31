package com.unicred.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketDTO {

    @JsonProperty("valor")
    @NotNull(message = "O valor do boleto deve ser informado")
    private BigDecimal value;

    @JsonProperty("vencimento")
    @FutureOrPresent(message = "A data de vencimento não pode ser menor que a data atual")
    private LocalDate dueDate;

}
