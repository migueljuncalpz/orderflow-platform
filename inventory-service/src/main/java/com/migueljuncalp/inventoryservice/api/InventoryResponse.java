package com.migueljuncalp.inventoryservice.api;

import com.migueljuncalp.inventoryservice.domain.Inventory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Stock materializado de un producto")
public record InventoryResponse(String productId, int quantity, long version) {

    public static InventoryResponse from(Inventory inventory) {
        return new InventoryResponse(inventory.getProductId(), inventory.getAvailableQuantity(), inventory.getVersion());
    }
}
