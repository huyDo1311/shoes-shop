package com.cybersoft.shop.seed;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageSeed {
    private final String imageUrl;
    public ProductImageSeed(String imageUrl){ this.imageUrl = imageUrl; }
}
