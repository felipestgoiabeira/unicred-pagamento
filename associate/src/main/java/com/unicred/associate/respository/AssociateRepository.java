package com.unicred.associate.respository;

import com.unicred.associate.domain.Associate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssociateRepository extends JpaRepository<Associate, UUID> {
}
