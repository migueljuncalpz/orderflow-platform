package com.migueljuncalp.inventoryservice.repository;

import com.migueljuncalp.inventoryservice.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
}
