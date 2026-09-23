package com.migueljuncalp.inventoryservice.messaging.consumer;

import com.migueljuncalp.eventcontracts.inventory.v1.StockMovementEventV1;
import com.migueljuncalp.inventoryservice.config.KafkaConfig;
import com.migueljuncalp.inventoryservice.service.StockMovementProcessor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StockMovementConsumer {

    private final StockMovementProcessor processor;

    public StockMovementConsumer(StockMovementProcessor processor) {
        this.processor = processor;
    }

    @KafkaListener(topics = KafkaConfig.STOCK_MOVEMENTS_TOPIC, groupId = "inventory-service")
    public void consume(StockMovementEventV1 event) {
        processor.process(event);
    }
}
