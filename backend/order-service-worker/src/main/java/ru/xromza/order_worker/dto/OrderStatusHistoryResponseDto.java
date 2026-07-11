package ru.xromza.order_worker.dto;

import ru.xromza.order_worker.utils.Status;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(
    description = "История изменения статуса заказа",
    example = """
        {
          "orderId": 69,
          "status": "SHIPPED",
          "changedAt": "2026-04-15T13:37:00",
          "changedByName": "Madin",
          "comment": "Заказ отправлен"
        }
        """
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class OrderStatusHistoryResponseDto {
    @Schema(description = "Идентификатор заказа", example = "69")
    private String orderId;
    @Schema(description = "Статус заказа", example = "SHIPPED")
    private Status status;
    @Schema(description = "Дата и время изменения статуса", example = "2026-04-15T13:37:00")
    private LocalDateTime changedAt;
    @Schema(description = "Id пользователя, изменившего статус", example = "3", nullable = true)
    private Long changedById;
    private String changedByName;
    @Schema(description = "Комментарий к изменению", example = "Заказ отправлен")
    private String comment;
}