package com.migueljuncalp.inventoryservice.domain;

import com.migueljuncalp.eventcontracts.inventory.v1.MovementType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "inventories")
public class Inventory {

    @Id
    private String productId;

    private int quantity;

    @Version
    private long version;

    protected Inventory() {
    }

    public Inventory(String productId) {
        this.productId = productId;
        this.quantity = 0;
    }

    public void apply(MovementType type, int amount) {
        var nextQuantity = type == MovementType.IN ? quantity + amount : quantity - amount;
        if (nextQuantity < 0) {
            throw new InsufficientStockException(productId, quantity, amount);
        }
        quantity = nextQuantity;
    }

    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public long getVersion() { return version; }
}
