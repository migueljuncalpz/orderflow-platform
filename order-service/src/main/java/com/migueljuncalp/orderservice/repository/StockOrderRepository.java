package com.migueljuncalp.orderservice.repository;

import com.migueljuncalp.orderservice.domain.StockOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockOrderRepository extends JpaRepository<StockOrder, UUID> {
}
