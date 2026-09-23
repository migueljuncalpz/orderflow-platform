package com.migueljuncalp.orderservice.messaging.consumer;

import com.migueljuncalp.eventcontracts.orders.v1.StockOrderEventV1;
import com.migueljuncalp.orderservice.config.KafkaConfig;
import com.migueljuncalp.orderservice.config.InventoryProperties;
import com.migueljuncalp.orderservice.service.ProcessingInterruptedException;
import com.migueljuncalp.orderservice.service.StockMovementProcessor;
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

    @KafkaListener(topics = KafkaConfig.STOCK_ORDERS_TOPIC, groupId = "inventory-service")
    public void consume(StockOrderEventV1 event) {
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
