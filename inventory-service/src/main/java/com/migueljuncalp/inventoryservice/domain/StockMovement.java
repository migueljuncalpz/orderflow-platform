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
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    private UUID id;

    private String productId;

    @Enumerated(EnumType.STRING)
    private MovementType type;

    private int quantity;

    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private MovementStatus status;

    protected StockMovement() {
    }

    public StockMovement(UUID id, String productId, MovementType type, int quantity,
                         Instant createdAt, MovementStatus status) {
        this.id = id;
        this.productId = productId;
        this.type = type;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.status = status;
    }

    public UUID getId() { return id; }
    public String getProductId() { return productId; }
    public MovementType getType() { return type; }
    public int getQuantity() { return quantity; }
    public Instant getCreatedAt() { return createdAt; }
    public MovementStatus getStatus() { return status; }
}
