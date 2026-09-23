package com.migueljuncalp.inventoryservice.repository;

import com.migueljuncalp.inventoryservice.domain.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT inventory
            FROM Inventory inventory
            WHERE inventory.productId IN :productIds
            ORDER BY inventory.productId
            """)
    List<Inventory> findAllByProductIdInForUpdate(
            @Param("productIds") Collection<String> productIds
    );
}
