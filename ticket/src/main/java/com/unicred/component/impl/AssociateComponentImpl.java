package com.unicred.component.impl;

import com.unicred.component.AssociateComponent;
import com.unicred.component.dto.AssociateResponseDTO;
import com.unicred.config.AppConfiguration;
import com.unicred.domain.component.AssociateResponse;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.exception.ExpectationFailedException;
import com.unicred.mapper.AssociateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssociateComponentImpl implements AssociateComponent {

    private final RestTemplate restTemplate;
    private final AppConfiguration appConfiguration;
    private final AssociateMapper associateMapper;

    @Override
    public AssociateResponse getAssociate(UUID uuid) throws
            ExpectationFailedException, EntityNotFoundException {

        try {
            var associate = restTemplate
                    .getForEntity(appConfiguration.getAssociateApiUrl() + "/" + uuid, AssociateResponseDTO.class);

            return associateMapper.toAssociateResponse(associate.getBody());

        } catch (HttpClientErrorException.NotFound exception) {

            throw new EntityNotFoundException("Associado não encontrado");
        } catch (RestClientException exception) {
            log.error("Error:", exception);
            throw new ExpectationFailedException();
        }
    }

    @Override
    public AssociateResponseDTO getAssociateByDocument(String associateDocument) throws
            EntityNotFoundException, ExpectationFailedException {
        try {
            var associate = restTemplate
                    .getForEntity(appConfiguration.getAssociateApiUrl() + "/documento/" + associateDocument, AssociateResponseDTO.class);

            return associate.getBody();

        } catch (HttpClientErrorException.NotFound exception) {
            throw new EntityNotFoundException("Associado não encontrado");
        } catch (RestClientException exception) {
            throw new ExpectationFailedException();
        }
    }


}
