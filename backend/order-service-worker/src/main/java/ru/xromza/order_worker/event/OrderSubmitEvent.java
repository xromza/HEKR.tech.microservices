package ru.xromza.order_worker.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.xromza.order_worker.model.OrderItemEventModel;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSubmitEvent {
    private String orderId;
    private Long userId;
    private Long warehouseId;
    private String address;
    private String paymentMethod;
    private List<OrderItemEventModel> items;
    private String comment;
}
