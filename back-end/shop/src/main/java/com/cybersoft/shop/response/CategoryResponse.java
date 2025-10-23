package com.cybersoft.shop.response;

import com.cybersoft.shop.entity.Brand;
import com.cybersoft.shop.entity.Category;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CategoryResponse {
    private int id;
    private String categoryName;

    public static CategoryResponse toDTO(Category category){
        return  CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }
}
