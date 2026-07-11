package ru.xromza.order_worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.xromza.order_worker.model.OrderStatusHistory;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, String> {
    
}
