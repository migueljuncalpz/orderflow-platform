package com.migueljuncalp.orderservice.messaging.producer;

import com.migueljuncalp.eventcontracts.orders.v1.StockOrderEventV1;
import com.migueljuncalp.orderservice.config.KafkaConfig;
import com.migueljuncalp.orderservice.messaging.EventPublicationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class StockOrderProducer {

    private final KafkaTemplate<String, StockOrderEventV1> kafkaTemplate;

    public StockOrderProducer(KafkaTemplate<String, StockOrderEventV1> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(StockOrderEventV1 event) {
        try {
            kafkaTemplate.send(KafkaConfig.STOCK_ORDERS_TOPIC, event.eventId().toString(), event)
                    .get(5, TimeUnit.SECONDS);
        } catch (Exception exception) {
            throw new EventPublicationException("No se pudo publicar el movimiento", exception);
        }
    }
}
