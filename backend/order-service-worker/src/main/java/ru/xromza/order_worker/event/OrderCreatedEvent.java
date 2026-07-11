package ru.xromza.order_worker.event;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import ru.xromza.order_worker.dto.OrderItemEventDto;

@Builder
@Getter
public class OrderCreatedEvent {
    private String orderId;
    private List<OrderItemEventDto> items;
    private Long warehouseId;
}
