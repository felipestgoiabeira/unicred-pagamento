package com.unicred.component.impl.impl;

import com.unicred.component.TicketComponent;
import com.unicred.component.impl.dto.TicketResponseDTO;
import com.unicred.config.AppConfiguration;
import com.unicred.domain.component.TicketResponse;
import com.unicred.exception.ExpectationFailedException;
import com.unicred.mapper.AssociateTicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TicketComponentImpl implements TicketComponent {

    private final RestTemplate restTemplate;
    private final AppConfiguration appConfiguration;
    private final AssociateTicketMapper associateTicketMapper;

    @Override
    public TicketResponse getTickets(UUID associateUUID) throws
            ExpectationFailedException {
        try {
            var ticketResponse = restTemplate
                    .getForEntity(appConfiguration.getTicketApiUrl() + "/" + associateUUID, TicketResponseDTO.class);

            return associateTicketMapper.toTicketResponse(ticketResponse.getBody());

        } catch (RestClientException exception) {
            throw new ExpectationFailedException();
        }
    }
}
