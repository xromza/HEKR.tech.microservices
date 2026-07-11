package ru.xromza.order_worker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.xromza.order_worker.event.OrderSubmitEvent;
import ru.xromza.order_worker.model.Order;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "id", source = "orderId")
    @Mapping(target = "warehouseId", source = "warehouseId")
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "address", source = "address")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "comment", source = "comment")
    @Mapping(target = "history", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order toOrder(OrderSubmitEvent event);
}
