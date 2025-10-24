package com.cybersoft.shop.response.variant;

import com.cybersoft.shop.entity.Variant;
import com.cybersoft.shop.response.ColorResponse;
import com.cybersoft.shop.response.SizeResponse;
import lombok.Builder;
import lombok.Data;
import org.hibernate.Internal;

import java.util.List;

@Data
@Builder
public class VariantResponse {
    private String sku;
    private int quantity;
    private String color;
    private String size;

    public static VariantResponse toDTO(Variant variant) {
        return VariantResponse.builder()
                .sku(variant.getSku())
                .quantity(variant.getQuantity())
                .color(variant.getColor().getColorName())
                .size(String.valueOf(variant.getSize().getSizeValue()))
                .build();
    }
}
