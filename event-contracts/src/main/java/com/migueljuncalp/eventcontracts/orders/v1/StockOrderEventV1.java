package com.migueljuncalp.eventcontracts.orders.v1;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record StockOrderEventV1(
        UUID eventId,
        UUID orderId,
        List<OrderItemEventV1> items,
        Instant occurredAt
) {
}
