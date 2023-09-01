package com.unicred.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum TicketStatus {
    AWAITING_PAYMENT("AGUARDANDO_PAGAMENTO"),
    PAID("PAGO"),
    EXPIRED("VENCIDO");

    private String description;

    TicketStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }

    public static TicketStatus of(String code){
        return Stream.of(TicketStatus.values())
                .filter(c -> c.getDescription().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
