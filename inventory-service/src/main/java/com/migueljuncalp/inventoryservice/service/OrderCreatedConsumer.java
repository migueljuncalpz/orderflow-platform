package com.migueljuncalp.inventoryservice.service;

import com.migueljuncalp.eventcontracts.orders.v1.StockOrderEventV1;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

    private final StockMovementService movementService;

    public OrderCreatedConsumer(
            StockMovementService reservationService
    ) {
        this.movementService = reservationService;
    }

    @KafkaListener(
            topics = "stock-orders",
            groupId = "inventory-service"
    )
    public void consume(StockOrderEventV1 event) {
//        movementService.(
//                event.orderId(),
//                event.items()
//        );
    }
}