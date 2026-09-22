package com.migueljuncalp.orderservice.mapper;

import com.migueljuncalp.eventcontracts.orders.v1.StockOrderEventV1;
import com.migueljuncalp.orderservice.domain.Item;
import com.migueljuncalp.orderservice.domain.OrderStatus;
import com.migueljuncalp.orderservice.domain.StockOrder;
import org.springframework.stereotype.Component;

@Component
public class StockOrderEventV1Mapper {

    public StockOrder toEntity(StockOrderEventV1 event) {
        var items = event.items().stream()
                .map(item -> new Item(
                        item.productId(),
                        item.quantity()
                ))
                .toList();

        return new StockOrder(
                event.orderId(),
                items,
                event.occurredAt(),
                OrderStatus.PENDING
        );
    }
}