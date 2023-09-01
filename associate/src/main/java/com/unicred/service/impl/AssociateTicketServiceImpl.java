package com.unicred.service.impl;

import com.unicred.config.AppConfiguration;
import com.unicred.domain.Ticket;
import com.unicred.exception.EntityNotFoundException;
import com.unicred.respository.AssociateRepository;
import com.unicred.service.AssociateTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssociateTicketServiceImpl implements AssociateTicketService {

    private final String MESSAGE_NOT_FOUND = "Associado não encontrado";

    private final AssociateRepository associateRepository;

    private final KafkaTemplate<String, Ticket> producer;

    private final AppConfiguration appConfiguration;

    @Override
    public void createTickets(UUID id, List<Ticket> tickets) throws EntityNotFoundException {
        var associateOptional = associateRepository.findById(id);

        if (associateOptional.isEmpty()) {
            throw new EntityNotFoundException(MESSAGE_NOT_FOUND);
        }

        tickets.parallelStream().forEach(ticket -> producer
                .send(appConfiguration.getTopicCreateTicket(), String.valueOf(ticket.hashCode()), ticket));

    }

    @Override
    public void generate(UUID id, Integer amount, LocalDate dueDate) throws EntityNotFoundException {
        var associateOptional = associateRepository.findById(id);

        if (associateOptional.isEmpty()) {
            throw new EntityNotFoundException(MESSAGE_NOT_FOUND);
        }

        IntStream.range(0, amount).forEach(
                number -> {

                    var ticket = Ticket.builder()
                            .associateUUID(id)
                            .value(generateRandomBigDecimal())
                            .dueDate(dueDate)
                            .build();

                    producer
                            .send(appConfiguration.getTopicCreateTicket(), String.valueOf(ticket.hashCode()), ticket);

                }
        );

    }

    public static BigDecimal generateRandomBigDecimal() {
        var minValue = BigDecimal.ZERO;
        var maxValue = new BigDecimal("100000");
        var scale = 2;
        var random = new Random();

        var randomBigDecimal = minValue
                .add(BigDecimal.valueOf(random.nextDouble())
                        .multiply(maxValue.subtract(minValue)));

        return randomBigDecimal.setScale(scale, RoundingMode.HALF_UP);
    }
}
