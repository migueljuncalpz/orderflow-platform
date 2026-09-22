package com.migueljuncalp.orderservice.mapper;

import com.migueljuncalp.orderservice.api.CreateOrderRequest;
import com.migueljuncalp.orderservice.domain.Item;
import com.migueljuncalp.orderservice.domain.OrderStatus;
import com.migueljuncalp.orderservice.domain.StockOrder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Component
public class OrderMapper {

    public StockOrder toEntity(CreateOrderRequest request) {
        var order = new StockOrder(
                UUID.randomUUID(),
                new ArrayList<>(),
                Instant.now(),
                OrderStatus.PENDING
                );

        request.items().forEach(item ->
                order.getItems().add(new Item(
                        item.productId(),
                        item.quantity()
                ))
        );

        return order;
    }
}