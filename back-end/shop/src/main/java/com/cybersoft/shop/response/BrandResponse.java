package com.cybersoft.shop.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BrandResponse {
    private int id;
    private String brandName;
}
