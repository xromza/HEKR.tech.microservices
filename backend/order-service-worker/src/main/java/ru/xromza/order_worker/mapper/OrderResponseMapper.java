package ru.xromza.order_worker.mapper;

import ru.xromza.order_worker.dto.OrderResponseDto;
import ru.xromza.order_worker.model.Order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, OrderStatusHistoryMapper.class})
public interface OrderResponseMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "warehouseId", source = "warehouseId")
    @Mapping(target = "totalPrice", source = "price")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "paymentMethod", source = "payment")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "statusHistory", source = "history")
    @Mapping(target = "comment", source="comment")
    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toDtoList(List<Order> orders);
}