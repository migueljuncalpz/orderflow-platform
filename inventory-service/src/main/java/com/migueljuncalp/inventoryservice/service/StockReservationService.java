package com.migueljuncalp.inventoryservice.service;

import com.migueljuncalp.eventcontracts.inventory.v1.MovementType;
import com.migueljuncalp.eventcontracts.orders.v1.OrderItemEventV1;
import com.migueljuncalp.eventcontracts.orders.v1.StockOrderEventV1;
import com.migueljuncalp.inventoryservice.domain.MovementStatus;
import com.migueljuncalp.inventoryservice.domain.ReservationResult;
import com.migueljuncalp.inventoryservice.domain.StockMovement;
import com.migueljuncalp.inventoryservice.repository.InventoryRepository;
import com.migueljuncalp.inventoryservice.repository.StockMovementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.UUID;

@Service
public class StockReservationService {
    private final InventoryRepository inventoryRepository;
    private final StockMovementRepository movementRepository;
    private final Clock clock;

    public StockReservationService(
            InventoryRepository inventoryRepository,
            StockMovementRepository movementRepository,
            Clock clock
    ) {
        this.inventoryRepository = inventoryRepository;
        this.movementRepository = movementRepository;
        this.clock = clock;
    }

    @Transactional
    public ReservationResult reserve(StockOrderEventV1 event) {
        var requestedItems = event.items().stream().map(OrderItemEventV1::productId).sorted().toList();

        var inventories = inventoryRepository.findAllByProductIdInForUpdate(requestedItems);

        boolean hasEnoughStock = event.items().stream()
                .allMatch(item -> inventories.stream()
                        .filter(inventory ->
                                inventory.getProductId().equals(item.productId())
                        )
                        .anyMatch(inventory ->
                                inventory.hasAvailable(item.quantity())
                        ));


        for (var item : event.items()) {
            var inventory = inventories.stream()
                    .filter(current ->
                            current.getProductId().equals(item.productId())
                    )
                    .findFirst()
                    .orElseThrow();

            inventory.reserve(item.quantity());

            var movement = new StockMovement(
                    UUID.randomUUID(),
                    item.productId(),
                    MovementType.RESERVATION,
                    item.quantity(),
                    clock.instant(),
                    MovementStatus.PUBLISHED
            );

            movementRepository.save(movement);
        }

        return ReservationResult.reserved(event.orderId());
    }
}
