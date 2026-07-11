package ru.xromza.order_worker.model;

import ru.xromza.order_worker.utils.PaymentMethod;
import ru.xromza.order_worker.utils.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")

public class Order {
    @Id
    private String id;
    private Long userId;
    private Long warehouseId;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;
    @Column(nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "payment_method", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private PaymentMethod payment;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(columnDefinition = "TEXT")
    private String comment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<OrderItem> items = new LinkedHashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<OrderStatusHistory> history = new LinkedHashSet<>();

    public void addItem(OrderItem item) {
        if (this.items == null)
            this.items = new LinkedHashSet<>();
        items.add(item);
        item.setOrder(this);
    }

    public void addHistory(OrderStatusHistory history) {
        if (this.history == null)
            this.history = new LinkedHashSet<>();
        this.history.add(history);
        history.setOrder(this);
    }

}