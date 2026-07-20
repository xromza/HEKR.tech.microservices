package ru.xromza.order.event;

import lombok.Getter;

@Getter
public class OrderStatusUpdatedEvent {
    private String orderId;
    private String status;
}
