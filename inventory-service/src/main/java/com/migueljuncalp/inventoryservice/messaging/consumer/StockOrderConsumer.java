package com.migueljuncalp.inventoryservice.messaging.consumer;

import com.migueljuncalp.eventcontracts.inventory.v1.InventoryReservationResultEventV1;
import com.migueljuncalp.eventcontracts.orders.v1.StockOrderEventV1;
import com.migueljuncalp.inventoryservice.domain.ReservationResult;
import com.migueljuncalp.inventoryservice.messaging.producer.ReservationResultProducer;
import com.migueljuncalp.inventoryservice.service.StockReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.UUID;

@Component
public class StockOrderConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(StockOrderConsumer.class);

    private final StockReservationService stockReservationService;
    private final ReservationResultProducer resultProducer;
    private final Clock clock;

    public StockOrderConsumer(
            StockReservationService reservationService, ReservationResultProducer resultProducer, Clock clock
    ) {
        this.stockReservationService = reservationService;
        this.resultProducer = resultProducer;
        this.clock = clock;
    }

    @KafkaListener(
            topics = "stock-orders",
            groupId = "inventory-service"
    )
    public void consume(StockOrderEventV1 event) {
        log.info(
                "Procesando pedido eventId={}, orderId={}, items={}",
                event.eventId(),
                event.orderId(),
                event.items()
        );

        try {
            ReservationResult result = stockReservationService.reserve(event);

            log.info(
                    "Reserva procesada para orderId={}: {}",
                    event.orderId(),
                    result.status()
            );

            var resultEvent = new InventoryReservationResultEventV1(
                    UUID.randomUUID(),
                    result.orderId(),
                    result.status().name(),
                    result.reason(),
                    clock.instant()
            );

            resultProducer.publish(resultEvent);
        } catch (Exception exception) {
            log.error(
                    "Error procesando el pedido orderId={}",
                    event.orderId(),
                    exception
            );

            throw exception; // necesario para que Kafka reintente
        }
    }
}