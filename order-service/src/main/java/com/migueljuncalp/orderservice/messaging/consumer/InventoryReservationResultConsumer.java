package com.migueljuncalp.orderservice.messaging.consumer;

import com.migueljuncalp.eventcontracts.inventory.v1.InventoryReservationResultEventV1;
import com.migueljuncalp.orderservice.service.OrderReservationResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryReservationResultConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(InventoryReservationResultConsumer.class);

    private final OrderReservationResultService resultService;

    public InventoryReservationResultConsumer(
            OrderReservationResultService resultService
    ) {
        this.resultService = resultService;
    }

    @KafkaListener(
            topics = "inventory.reservation-results",
            groupId = "order-service"
    )
    public void consume(InventoryReservationResultEventV1 event) {
        log.info(
                "Resultado de reserva recibido: orderId={}, status={}",
                event.orderId(),
                event.status()
        );

        resultService.process(event);

        log.info(
                "Estado del pedido {} actualizado correctamente",
                event.orderId()
        );
    }
}