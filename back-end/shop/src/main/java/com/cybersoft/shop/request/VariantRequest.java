package com.cybersoft.shop.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VariantRequest {
    private int productId;
    private int colorId;
    private int sizeId;
    private int quantity;
}
