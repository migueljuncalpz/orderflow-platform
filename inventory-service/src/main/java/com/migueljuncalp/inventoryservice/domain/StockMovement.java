package com.migueljuncalp.inventoryservice.domain;

import com.migueljuncalp.eventcontracts.inventory.v1.MovementType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "stock_movement")
public class StockMovement {

    @Id
    private UUID movementId;

    private String productId;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    private int quantity;

    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private MovementStatus movementStatus;

    protected StockMovement() {
    }

    public StockMovement(UUID id, String productId, MovementType type, int quantity,
                         Instant createdAt, MovementStatus status) {
        this.movementId = id;
        this.productId = productId;
        this.movementType = type;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.movementStatus = status;
    }

    public UUID getMovementId() { return movementId; }
    public String getProductId() { return productId; }
    public MovementType getMovementType() { return movementType; }
    public int getQuantity() { return quantity; }
    public Instant getCreatedAt() { return createdAt; }
    public MovementStatus getMovementStatus() { return movementStatus; }
}
