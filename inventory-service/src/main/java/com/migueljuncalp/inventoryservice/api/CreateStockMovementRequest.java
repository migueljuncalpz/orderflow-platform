package com.migueljuncalp.inventoryservice.api;

import com.migueljuncalp.eventcontracts.inventory.v1.MovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Petición para registrar una entrada o salida de stock")
public record CreateStockMovementRequest(
        @Schema(description = "Identificador del producto", example = "SKU-123")
        @NotBlank(message = "productId es obligatorio")
        @Pattern(regexp = "[A-Za-z0-9_-]{3,50}", message = "productId tiene un formato no válido")
        String productId,

        @Schema(description = "Tipo de movimiento", example = "IN")
        @NotNull(message = "type es obligatorio")
        MovementType type,

        @Schema(description = "Número de unidades", example = "10", minimum = "1")
        @Min(value = 1, message = "quantity debe ser mayor que cero")
        int quantity
) {
}
