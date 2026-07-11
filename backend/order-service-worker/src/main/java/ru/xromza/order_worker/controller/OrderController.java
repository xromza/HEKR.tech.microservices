package ru.xromza.order_worker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.xromza.order_worker.dto.OrderResponseDto;
import ru.xromza.order_worker.service.OrderService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-worker/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ServiceExchange getAllOrders(@RequestHeader("X-User-Id") Long userId) {
        List<OrderResponseDto> orders = orderService.getOrders(userId);
        return new ServiceExchange(orders);
    }

    @GetMapping("/{orderId}")
    public ServiceExchange getOrder(@PathVariable String orderId) {
        List<OrderResponseDto> orders = List.of(orderService.getOrder(orderId));
        return new ServiceExchange(orders);
    }
    

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ServiceExchange {
        public List<OrderResponseDto> order;
    }

}
