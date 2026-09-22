package com.migueljuncalp.inventoryservice.service;

import com.migueljuncalp.eventcontracts.inventory.v1.StockMovementEventV1;
import com.migueljuncalp.inventoryservice.domain.Inventory;
import com.migueljuncalp.inventoryservice.domain.ProcessedEvent;
import com.migueljuncalp.inventoryservice.repository.InventoryRepository;
import com.migueljuncalp.inventoryservice.repository.ProcessedEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class StockMovementProcessor {

    private final InventoryRepository inventoryRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final Clock clock;

    public StockMovementProcessor(InventoryRepository inventoryRepository,
                                  ProcessedEventRepository processedEventRepository,
                                  Clock clock) {
        this.inventoryRepository = inventoryRepository;
        this.processedEventRepository = processedEventRepository;
        this.clock = clock;
    }

    @Transactional
    public void  process(StockMovementEventV1 event) {
        if (processedEventRepository.existsById(event.eventId())) {
            return;
        }

        var inventory = inventoryRepository.findById(event.productId())
                .orElseGet(() -> new Inventory(event.productId()));
        inventory.apply(event.type(), event.quantity());

        inventoryRepository.save(inventory);
        processedEventRepository.save(new ProcessedEvent(event.eventId(), clock.instant()));
    }
}
