package ru.xromza.order.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.xromza.order.dto.OrderPreviewRequestDto;
import ru.xromza.order.dto.OrderRequestDto;
import ru.xromza.order.dto.OrderResponseDto;
import ru.xromza.order.dto.OrderSubmittedDto;
import ru.xromza.order.dto.PreOrderResponseDto;
import ru.xromza.order.service.OrderService;
import ru.xromza.order.utils.Status;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderSubmittedDto> createOrder(@Valid @RequestBody OrderRequestDto dto,
            @RequestHeader("X-User-Id") Long userId) {
        String uuid = orderService.createOrder(dto, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                OrderSubmittedDto.builder()
                        .orderId(uuid)
                        .status(Status.PROCESSING)
                        .build());
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable String orderId,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(orderService.getOrder(orderId, userId));
    }

    @PostMapping("/preview")
    public ResponseEntity<PreOrderResponseDto> getOrderPreview(@RequestBody OrderPreviewRequestDto dto) {
        return ResponseEntity.ok(orderService.getOrderPreview(dto));
    }

}
