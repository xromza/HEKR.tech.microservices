package ru.xromza.warehouse.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.xromza.warehouse.dto.OrderItemEventDto;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private String orderId;
    private List<OrderItemEventDto> items;
    private Long warehouseId;
}
