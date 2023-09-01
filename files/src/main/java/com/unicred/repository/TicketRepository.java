package com.unicred.repository;

import com.unicred.domain.Ticket;
import com.unicred.domain.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Optional<Ticket> findByUuidAndAssociateUUID(UUID uuid, UUID associatedUUID);

}
