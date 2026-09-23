package com.migueljuncalp.inventoryservice.messaging.producer;

import com.migueljuncalp.eventcontracts.inventory.v1.InventoryReservationResultEventV1;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReservationResultProducer {

    public static final String TOPIC = "inventory.reservation-results";

    private final KafkaTemplate<String, InventoryReservationResultEventV1> kafkaTemplate;

    public ReservationResultProducer(
            KafkaTemplate<String, InventoryReservationResultEventV1> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(InventoryReservationResultEventV1 event) {
        kafkaTemplate.send(
                TOPIC,
                event.orderId().toString(), // clave Kafka
                event
        );
    }
}