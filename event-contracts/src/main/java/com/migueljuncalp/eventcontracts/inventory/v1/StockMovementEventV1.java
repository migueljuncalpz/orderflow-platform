package com.migueljuncalp.eventcontracts.inventory.v1;


import java.time.Instant;
import java.util.UUID;

public record StockMovementEventV1(
        UUID eventId,
        String productId,
        MovementType type,
        int quantity,
        Instant occurredAt
) {
}
