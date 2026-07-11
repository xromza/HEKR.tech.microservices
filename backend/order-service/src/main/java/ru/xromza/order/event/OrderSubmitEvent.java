package ru.xromza.order.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.xromza.order.model.OrderItem;
import ru.xromza.order.utils.PaymentMethod;

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
    private PaymentMethod paymentMethod;
    private List<OrderItem> items;
    private String comment;
}
