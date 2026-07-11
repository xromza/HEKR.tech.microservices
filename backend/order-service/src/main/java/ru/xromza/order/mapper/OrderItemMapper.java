package ru.xromza.order.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import ru.xromza.order.dto.OrderItemRequestDto;
import ru.xromza.order.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem toEventItem(OrderItemRequestDto dto);

    List<OrderItem> toEventItemsList(List<OrderItemRequestDto> dtos);
}
