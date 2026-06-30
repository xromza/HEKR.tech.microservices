package ru.xromza.catalog.model;

import ru.xromza.catalog.utils.ImageType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="images", schema = "catalog-schema")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String url;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "TEXT")
    private ImageType type;
    @Column(name="sort_order")
    private Integer sortOrder;
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="variant_id",nullable = false)
    private ProductVariant variant;
}