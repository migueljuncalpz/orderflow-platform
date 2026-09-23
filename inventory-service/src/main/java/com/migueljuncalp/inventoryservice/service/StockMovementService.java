package com.migueljuncalp.inventoryservice.service;

import com.migueljuncalp.eventcontracts.inventory.v1.StockMovementEventV1;
import com.migueljuncalp.inventoryservice.api.CreateStockMovementRequest;
import com.migueljuncalp.inventoryservice.api.ResourceNotFoundException;
import com.migueljuncalp.inventoryservice.api.StockMovementResponse;
import com.migueljuncalp.inventoryservice.domain.MovementStatus;
import com.migueljuncalp.inventoryservice.domain.StockMovement;
import com.migueljuncalp.inventoryservice.messaging.producer.StockMovementProducer;
import com.migueljuncalp.inventoryservice.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class StockMovementService {

    private final StockMovementRepository repository;
    private final StockMovementProducer producer;
    private final Clock clock;

    public StockMovementService(StockMovementRepository repository, StockMovementProducer producer, Clock clock) {
        this.repository = repository;
        this.producer = producer;
        this.clock = clock;
    }

    @Transactional
    public StockMovementResponse create(CreateStockMovementRequest request) {
        UUID id = UUID.randomUUID();
        Instant now = clock.instant();
        var movement = new StockMovement(id, request.productId(), request.type(), request.quantity(),
                now, MovementStatus.PUBLISHED);

        repository.save(movement);
        producer.publish(new StockMovementEventV1(id, request.productId(), request.type(), request.quantity(), now));
        return StockMovementResponse.from(movement);
    }

    @Transactional(readOnly = true)
    public StockMovementResponse findById(UUID id) {
        return repository.findById(id)
                .map(StockMovementResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el movimiento " + id));
    }
}
