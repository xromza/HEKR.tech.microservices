package ru.xromza.warehouse.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public record StockId (
    Long variantId, 
    Long warehouseId
) implements Serializable {};