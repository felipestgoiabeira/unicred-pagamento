package com.unicred.respository;

import com.unicred.domain.Associate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssociateRepository extends JpaRepository<Associate, UUID> {

    Optional<Associate> findByDocument(String document);

}
