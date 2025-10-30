package com.cybersoft.shop.entity;

import com.cybersoft.shop.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(nullable = false)
    private float total = 0f;

    @Column(nullable = false)
    private boolean stockDeducted = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.Pending;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderVariant> items = new ArrayList<>();

    @Builder.Default
    @Column(name = "vnp_txn_ref")
    private String vnpTxnRef = "";

    public void recalcTotal() {
        if (items == null) { this.total = 0f; return; }
        float sum = 0f;
        for (OrderVariant i : items) sum += i.getPrice() * i.getQuantity();
        this.total = sum;
    }

    public void addOrIncreaseItem(String sku, float price, int quantity){
        if(items == null) items = new ArrayList<>();
        for(OrderVariant ov : items){
            if(ov.getVariantSku().equals(sku)){
                ov.setQuantity(ov.getQuantity() + quantity);
                return;
            }
        }
        OrderVariant newItem = OrderVariant.builder()
                .order(this)
                .variantSku(sku)
                .price(price)
                .quantity(quantity)
                .build();
        items.add(newItem);
    }
}
