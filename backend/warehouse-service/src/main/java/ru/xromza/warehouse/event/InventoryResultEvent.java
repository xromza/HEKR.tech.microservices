package ru.xromza.warehouse.event;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResultEvent {
    private String orderId;
    private String status;
    private String reason;
    private Map<Long, String> itemsError;
}
