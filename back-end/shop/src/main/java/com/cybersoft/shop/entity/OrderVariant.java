package com.cybersoft.shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "order_variant",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_order_variant_sku",
                columnNames = {"order_id","variant_sku"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderVariant extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "variant_sku",nullable = false,length = 100)
    private String variantSku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    private int quantity;
}
