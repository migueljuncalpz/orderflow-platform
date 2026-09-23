package com.migueljuncalp.inventoryservice.domain;

import com.migueljuncalp.eventcontracts.inventory.v1.MovementType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private String productId;

    private int availableQuantity;

    private int reservedQuantity;

    @Version
    private long version;

    protected Inventory() {
    }

    public Inventory(String productId) {
        this.productId = productId;
        this.availableQuantity = 0;
    }

    public void apply(MovementType type, int amount) {
        var nextQuantity = type == MovementType.IN ? availableQuantity + amount : availableQuantity - amount;
        if (nextQuantity < 0) {
            throw new InsufficientStockException(productId, availableQuantity, amount);
        }
        availableQuantity = nextQuantity;
    }
    public boolean hasAvailable(int requestedQuantity) {
        return requestedQuantity > 0
                && availableQuantity >= requestedQuantity;
    }

    public void reserve(int requestedQuantity) {
        if (!hasAvailable(requestedQuantity)) {
            throw new InsufficientStockException(
                    productId,
                    availableQuantity,
                    requestedQuantity
            );
        }

        availableQuantity -= requestedQuantity;
        reservedQuantity += requestedQuantity;
    }

    public void release(int quantity) {
        if (quantity <= 0 || quantity > reservedQuantity) {
            throw new IllegalArgumentException(
                    "Cantidad reservada no válida"
            );
        }

        reservedQuantity -= quantity;
        quantity += quantity;
    }

    public String getProductId() { return productId; }
    public int getAvailableQuantity() { return availableQuantity; }
    public long getVersion() { return version; }
}
