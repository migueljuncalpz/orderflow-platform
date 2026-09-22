package com.migueljuncalp.inventoryservice.service;

import com.migueljuncalp.eventcontracts.inventory.v1.StockMovementEventV1;
import com.migueljuncalp.inventoryservice.config.KafkaConfig;
import com.migueljuncalp.inventoryservice.config.InventoryProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StockMovementConsumer {

    private final StockMovementProcessor processor;
    private final InventoryProperties properties;

    public StockMovementConsumer(StockMovementProcessor processor, InventoryProperties properties) {
        this.processor = processor;
        this.properties = properties;
    }

    @KafkaListener(topics = KafkaConfig.STOCK_MOVEMENTS_TOPIC, groupId = "inventory-service")
    public void consume(StockMovementEventV1 event) {
        simulateProcessingTime();
        processor.process(event);
    }

    private void simulateProcessingTime() {
        try {
            Thread.sleep(properties.processingDelay().toMillis());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ProcessingInterruptedException("Procesamiento interrumpido", exception);
        }
    }
}
