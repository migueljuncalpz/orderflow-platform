package com.migueljuncalp.inventoryservice.api;

import com.migueljuncalp.eventcontracts.inventory.v1.MovementType;
import com.migueljuncalp.inventoryservice.domain.MovementStatus;
import com.migueljuncalp.inventoryservice.domain.StockMovement;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Movimiento de stock aceptado por el servicio")
public record StockMovementResponse(
        UUID id,
        String productId,
        MovementType type,
        int quantity,
        Instant createdAt,
        MovementStatus status
) {
    public static StockMovementResponse from(StockMovement movement) {
        return new StockMovementResponse(movement.getId(), movement.getProductId(), movement.getType(),
                movement.getQuantity(), movement.getCreatedAt(), movement.getStatus());
    }
}
