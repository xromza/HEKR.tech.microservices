package ru.xromza.order_worker.mapper;

import ru.xromza.order_worker.dto.OrderStatusHistoryResponseDto;
import ru.xromza.order_worker.model.OrderStatusHistory;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderStatusHistoryMapper {
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "status", source = "newStatus")
    @Mapping(target = "changedAt", source = "changedAt")
    @Mapping(target = "changedById", source = "changedBy")
    @Mapping(target = "changedByName", ignore = true)
    @Mapping(target = "comment", source = "comment")
    OrderStatusHistoryResponseDto toDto(OrderStatusHistory history);

    List<OrderStatusHistoryResponseDto> toDtoList(List<OrderStatusHistory> histories);
}