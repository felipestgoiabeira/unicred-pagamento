package com.unicred.domain.converter;

import com.unicred.domain.TicketStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TicketStatusConverter implements AttributeConverter<TicketStatus, String> {
    @Override
    public String convertToDatabaseColumn(TicketStatus status) {
        if (status == null) {
            return null;
        }
        return status.getDescription();
    }

    @Override
    public TicketStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return TicketStatus.of(code);
    }
}