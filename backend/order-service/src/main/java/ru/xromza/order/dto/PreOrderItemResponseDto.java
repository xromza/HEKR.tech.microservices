package ru.xromza.order.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.xromza.order.interfaces.ProductInfoInterface;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class PreOrderItemResponseDto implements ProductInfoInterface {
    private Long productId;
    private Long variantId;
    private String brand;
    private String title;
    private String sku;
    private String size;
    private String color;
    private String mainImageUrl;
    private Integer quantity;
    private Integer maxAvailableQuantity;
    private boolean isAvailable;
    private PreOrderPriceResponseDto price;
    private BigDecimal subtotal;
    private List<ItemWarehouseAvailabilityResponseDto> availableAtWarehouses;
}
