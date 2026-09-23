package com.migueljuncalp.orderservice.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stock_order")
public class StockOrder {

    @Id
    private UUID orderId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "stock_order_item",
            joinColumns = @JoinColumn(name = "order_id")
    )
    private List<Item> items = new ArrayList<>();

    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    protected StockOrder() {
    }

    public StockOrder(UUID orderId, List<Item> items, Instant createdAt, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.items = items;
        this.createdAt = createdAt;
        this.orderStatus = orderStatus;
    }

    public UUID getOrderId() { return orderId; }
    public List<Item> getItems() { return items; }
    public Instant getCreatedAt() { return createdAt; }
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void confirm() {
        if (orderStatus != OrderStatus.PENDING) {
            return; // idempotencia ante eventos duplicados
        }

        orderStatus = OrderStatus.CONFIRMED;
    }

    public void reject() {
        if (orderStatus != OrderStatus.PENDING) {
            return;
        }

        orderStatus = OrderStatus.REJECTED;
    }
}
