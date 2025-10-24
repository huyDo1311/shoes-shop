package com.cybersoft.shop.response;

import com.cybersoft.shop.entity.Brand;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BrandResponse {

    private int id;
    private String brandName;

    public static BrandResponse toDTO(Brand brand){
        return  BrandResponse.builder()
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .build();
    }
}
