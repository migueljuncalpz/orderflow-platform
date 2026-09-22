package com.migueljuncalp.inventoryservice.domain;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String productId, int available, int requested) {
        super("Stock insuficiente para %s: disponible=%d, solicitado=%d"
                .formatted(productId, available, requested));
    }
}
