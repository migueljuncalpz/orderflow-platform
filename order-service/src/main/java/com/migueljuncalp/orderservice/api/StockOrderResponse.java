package com.migueljuncalp.orderservice.api;

import com.migueljuncalp.orderservice.domain.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "Movimiento de stock aceptado por el servicio")
public record StockOrderResponse(
        UUID orderId,
        List<Item> items,
        OrderStatus status,
        Instant createdAt
) {
    public static StockOrderResponse from(StockOrder order) {
        return new StockOrderResponse(order.getOrderId(), order.getItems(), order.getOrderStatus(),order.getCreatedAt());
    }
}
