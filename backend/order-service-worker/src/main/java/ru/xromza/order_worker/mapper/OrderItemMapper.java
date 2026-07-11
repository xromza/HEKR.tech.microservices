package ru.xromza.order_worker.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ru.xromza.order_worker.dto.OrderItemResponseDto;
import ru.xromza.order_worker.dto.OrderItemEventDto;
import ru.xromza.order_worker.model.OrderItem;
import ru.xromza.order_worker.model.OrderItemEventModel;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemEventDto toEventDto(OrderItemEventModel model);

    OrderItemEventModel toEventItem(OrderItemEventDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "priceAtPurchase", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    OrderItem toOrder(OrderItemEventModel eventModel);

    List<OrderItemEventModel> toEventItemsList(List<OrderItemEventDto> dtos);

    List<OrderItemEventDto> toEventItemsDto(List<OrderItemEventModel> models);

    @Mapping(target = "variantId", source = "id.variantId")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "appliedPrice", source = "priceAtPurchase")
    @Mapping(target = "subtotal", ignore = true)
    OrderItemResponseDto toResponse(OrderItem item);

    @AfterMapping
    default void setSubtotal(@MappingTarget OrderItemResponseDto.OrderItemResponseDtoBuilder dtoBuilder,
            OrderItem item) {
        dtoBuilder.subtotal(item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())));
    }
}
