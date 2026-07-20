package ru.xromza.order_worker.event;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderStatusUpdatedEvent {
    private String orderId;
    private String status;    
}
