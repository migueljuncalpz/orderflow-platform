package com.migueljuncalp.orderservice.service;

import com.migueljuncalp.eventcontracts.orders.v1.StockOrderEventV1;
import com.migueljuncalp.orderservice.domain.ProcessedEvent;
import com.migueljuncalp.orderservice.mapper.StockOrderEventV1Mapper;
import com.migueljuncalp.orderservice.repository.ProcessedEventRepository;
import com.migueljuncalp.orderservice.repository.StockOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class StockMovementProcessor {

    private final StockOrderRepository orderRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final Clock clock;
    private final StockOrderEventV1Mapper stockOrderEventMapper;

    public StockMovementProcessor(StockOrderRepository orderRepository,
                                  ProcessedEventRepository processedEventRepository,
                                  Clock clock, StockOrderEventV1Mapper stockOrderEventMapper) {
        this.orderRepository = orderRepository;
        this.processedEventRepository = processedEventRepository;
        this.clock = clock;
        this.stockOrderEventMapper = stockOrderEventMapper;
    }

    @Transactional
    public void process(StockOrderEventV1 event) {
        if (processedEventRepository.existsById(event.eventId())) {
            return;
        }

        var order = orderRepository.findById(event.eventId())
                .orElseGet(() -> stockOrderEventMapper.toEntity(event));


        orderRepository.save(order);
        processedEventRepository.save(new ProcessedEvent(event.eventId(), clock.instant()));
    }
}
