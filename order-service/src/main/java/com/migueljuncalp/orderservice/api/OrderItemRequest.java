package com.migueljuncalp.orderservice.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Producto incluido en un pedido")
public record OrderItemRequest(

        @Schema(
                description = "Identificador del producto",
                example = "SKU-123"
        )
        String productId,

        @Schema(
                description = "Cantidad solicitada",
                example = "2",
                minimum = "1"
        )
        int quantity
) {
}