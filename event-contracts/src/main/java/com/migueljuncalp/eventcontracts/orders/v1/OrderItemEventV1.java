package com.migueljuncalp.eventcontracts.orders.v1;

public record OrderItemEventV1(
        String productId,
        int quantity
) {
}