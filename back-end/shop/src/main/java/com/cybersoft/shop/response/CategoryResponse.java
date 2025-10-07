package com.cybersoft.shop.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CategoryResponse {
    private int id;
    private String categoryName;
}
