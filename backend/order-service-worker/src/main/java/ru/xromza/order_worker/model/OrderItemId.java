package ru.xromza.order_worker.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrderItemId(String orderId, Long variantId) implements Serializable {}