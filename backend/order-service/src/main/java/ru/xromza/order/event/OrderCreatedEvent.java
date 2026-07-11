package ru.xromza.order.event;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import ru.xromza.order.model.OrderItem;

@Builder
@Getter
public class OrderCreatedEvent {
    private List<OrderItem> items;
    private Long warehouseId;
}
