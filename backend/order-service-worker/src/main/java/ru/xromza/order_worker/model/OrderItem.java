package ru.xromza.order_worker.model;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @NotNull
    private Order order;
    @Column(name = "quantity", nullable = false)
    @NotNull
    private Integer quantity;
    @Column(name = "price_at_purchase", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal priceAtPurchase;
    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal totalPrice;
    @Column(name = "price_type", nullable = false)
    @NotNull
    private String priceType;
}