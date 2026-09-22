package com.migueljuncalp.inventoryservice.repository;

import com.migueljuncalp.inventoryservice.domain.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {
}
