package com.migueljuncalp.orderservice.service;

import com.migueljuncalp.eventcontracts.orders.v1.OrderItemEventV1;
import com.migueljuncalp.eventcontracts.orders.v1.StockOrderEventV1;
import com.migueljuncalp.orderservice.api.CreateOrderRequest;
import com.migueljuncalp.orderservice.api.ResourceNotFoundException;
import com.migueljuncalp.orderservice.api.StockOrderResponse;
import com.migueljuncalp.orderservice.domain.StockOrder;
import com.migueljuncalp.orderservice.mapper.OrderMapper;
import com.migueljuncalp.orderservice.messaging.producer.StockOrderProducer;
import com.migueljuncalp.orderservice.repository.StockOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class StockOrderService {

    private final StockOrderRepository repository;
    private final StockOrderProducer producer;
    private final Clock clock;
    private final OrderMapper orderMapper;


    public StockOrderService(StockOrderRepository repository, StockOrderProducer producer, Clock clock,
                             OrderMapper mapper) {
        this.repository = repository;
        this.producer = producer;
        this.clock = clock;
        this.orderMapper = mapper;
    }

    @Transactional
    public StockOrderResponse create(CreateOrderRequest request) {
        UUID eventId = UUID.randomUUID();
        Instant now = clock.instant();
        StockOrder order = orderMapper.toEntity(request);
        List<OrderItemEventV1> eventItems = order.getItems().stream()
                .map(item -> new OrderItemEventV1(
                        item.getProductId(),
                        item.getQuantity()
                ))
                .toList();
        repository.save(order);
        producer.publish(new StockOrderEventV1(eventId,order.getOrderId(),eventItems, now));
        return StockOrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public StockOrderResponse findById(UUID id) {
        return repository.findById(id)
                .map(StockOrderResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el movimiento " + id));
    }
}
