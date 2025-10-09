package com.cybersoft.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "brands", uniqueConstraints = @UniqueConstraint(columnNames = "brandName"))
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String brandName;
}
