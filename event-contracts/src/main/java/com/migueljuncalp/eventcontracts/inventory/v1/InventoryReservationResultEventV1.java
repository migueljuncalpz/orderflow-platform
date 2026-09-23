package com.migueljuncalp.eventcontracts.inventory.v1;

import java.time.Instant;
import java.util.UUID;

public record InventoryReservationResultEventV1(
        UUID eventId,
        UUID orderId,
        String status,
        String reason,
        Instant occurredAt
) {
}