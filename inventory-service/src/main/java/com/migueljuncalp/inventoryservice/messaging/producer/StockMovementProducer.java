package com.migueljuncalp.inventoryservice.messaging.producer;

import com.migueljuncalp.eventcontracts.inventory.v1.StockMovementEventV1;
import com.migueljuncalp.inventoryservice.config.KafkaConfig;
import com.migueljuncalp.inventoryservice.messaging.EventPublicationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class StockMovementProducer {

    private final KafkaTemplate<String, StockMovementEventV1> kafkaTemplate;

    public StockMovementProducer(KafkaTemplate<String, StockMovementEventV1> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(StockMovementEventV1 event) {
        try {
            kafkaTemplate.send(KafkaConfig.STOCK_MOVEMENTS_TOPIC, event.productId(), event)
                    .get(5, TimeUnit.SECONDS);
        } catch (Exception exception) {
            throw new EventPublicationException("No se pudo publicar el movimiento", exception);
        }
    }
}
