package com.cybersoft.shop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "sizes")
@Data
public class Sizes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "size_value")
    private int value;
}
