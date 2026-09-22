package com.migueljuncalp.orderservice.api;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Petición para registrar una entrada o salida de stock")
public record CreateOrderRequest(

        @ArraySchema(
                arraySchema = @Schema(
                        description = "Lista de productos incluidos en el pedido"
                ),
                schema = @Schema(
                        implementation = OrderItemRequest.class
                )
        )
        List<OrderItemRequest> items
) {
}