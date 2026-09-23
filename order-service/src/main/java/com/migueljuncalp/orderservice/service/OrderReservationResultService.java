package com.migueljuncalp.orderservice.service;

import com.migueljuncalp.eventcontracts.inventory.v1.InventoryReservationResultEventV1;
import com.migueljuncalp.eventcontracts.inventory.v1.ReservationStatus;
import com.migueljuncalp.orderservice.domain.StockOrder;
import com.migueljuncalp.orderservice.repository.StockOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrderReservationResultService {

    private final StockOrderRepository orderRepository;

    public OrderReservationResultService(
            StockOrderRepository orderRepository
    ) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void process(InventoryReservationResultEventV1 event) {
        StockOrder order = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el pedido " + event.orderId()
                ));

        if (event.status().equals(ReservationStatus.RESERVED.toString())) {
            order.confirm();
        } else {
            order.reject();
        }

        // No es imprescindible llamar a save().
        // JPA detectará el cambio al finalizar la transacción.
    }
}