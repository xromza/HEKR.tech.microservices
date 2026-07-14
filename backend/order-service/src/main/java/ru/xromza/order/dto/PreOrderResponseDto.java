package ru.xromza.order.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.xromza.order.interfaces.ProductItemsInterface;

@Getter
@Builder
@Setter
public class PreOrderResponseDto implements ProductItemsInterface<PreOrderItemResponseDto> {
    public BigDecimal totalPrice;

    @Builder.Default
    public List<PreOrderItemResponseDto> items = new ArrayList<>();
    @Builder.Default
    public List<PreOrderWarehouseResponseDto> warehouses = new ArrayList<>();

    public void addItem(PreOrderItemResponseDto item) {
        if (item == null) {
            return;
        }
        items.add(item);
    }
}
