package com.cybersoft.shop.response.category;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CategoryResponse {
    private int id;
    private String categoryName;
}
