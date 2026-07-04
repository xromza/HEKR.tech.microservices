package ru.xromza.catalog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.xromza.catalog.dto.CartItemRequestDto;
import ru.xromza.catalog.dto.CartResponseDto;
import ru.xromza.catalog.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping
    public ResponseEntity<CartResponseDto> changeQuantity(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CartItemRequestDto dto) {
        return ResponseEntity.ok(cartService.addOrUpdateItem(userId, dto));
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<Void> deleteSingle(
            @RequestHeader("X-User-Id") Long userId,
            Long variantId) {
        cartService.removeFromCart(userId, variantId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll(
            @RequestHeader("X-User-Id") Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
