package ru.xromza.order_worker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.xromza.order_worker.utils.Status;

@Builder
@Getter
@Setter
public class OrderSubmittedDto {
    private String orderId;
    private Status status;   
}
