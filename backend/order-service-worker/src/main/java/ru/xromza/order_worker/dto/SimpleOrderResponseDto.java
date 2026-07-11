package ru.xromza.order_worker.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import ru.xromza.order_worker.utils.PaymentMethod;
import ru.xromza.order_worker.utils.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleOrderResponseDto {
        @Schema(description = "Идентификатор заказа", example = "111")
    private Long id;
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long userId;
    @Schema(description = "Идентификатор склада", example = "7")
    private Long warehouseId;
    @Schema(description = "Общая стоимость заказа", example = "1337.00", minimum = "0.01")
    private BigDecimal totalPrice;
    @Schema(description = "Адрес доставки", example = "г. Токио, ул. Костенко 67, д.10")
    private String address;
    @Schema(description = "Способ оплаты", example = "CASH")
    private PaymentMethod paymentMethod;
    @Schema(description = "Статус заказа", example = "SHIPPED")
    private Status status;
    @Schema(description = "Дата создания заказа", example = "2026-04-14T12:30:00")
    private LocalDateTime date;
    @Schema(description = "Список товаров в заказе")
    private List<OrderItemResponseDto> items;
}